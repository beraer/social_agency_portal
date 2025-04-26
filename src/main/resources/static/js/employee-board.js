document.addEventListener('DOMContentLoaded', function() {
    // Initialize variables
    const createCardModal = new bootstrap.Modal(document.getElementById('createCardModal'));
    const createListModal = new bootstrap.Modal(document.getElementById('createListModal'));
    const addMemberModal = new bootstrap.Modal(document.getElementById('addMemberModal'));

    let currentCardId = null;
    let cardDetailModal = null;

    // Get CSRF token
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

    // Initialize drag and drop
    const lists = document.querySelectorAll('.list-cards');
    lists.forEach(list => {
        new Sortable(list, {
            group: 'cards',
            animation: 150,
            ghostClass: 'card-ghost',
            chosenClass: 'card-chosen',
            dragClass: 'card-drag',
            onEnd: async function(evt) {
                const cardId = evt.item.getAttribute('data-card-id');
                const newListId = evt.to.closest('.list').getAttribute('data-list-id');
                
                try {
                    const response = await fetch(`/api/cards/${cardId}/move`, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                            [csrfHeader]: csrfToken
                        },
                        body: JSON.stringify({
                            listId: newListId
                        })
                    });

                    if (!response.ok) {
                        showAlert('Error moving card. Please try again.', 'danger');
                        evt.from.appendChild(evt.item);
                    }
                } catch (error) {
                    console.error('Error:', error);
                    showAlert('Error moving card. Please try again.', 'danger');
                    evt.from.appendChild(evt.item);
                }
            }
        });
    });

    // Handle card creation modal
    document.querySelectorAll('.add-card-button').forEach(button => {
        button.addEventListener('click', function() {
            const listId = this.getAttribute('data-list-id');
            document.getElementById('listId').value = listId;
        });
    });

    // Handle form submissions
    document.getElementById('createCardForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const formData = new FormData(this);
        const data = {
            title: formData.get('title'),
            description: formData.get('description'),
            deadline: formData.get('deadline'),
            listId: formData.get('listId')
        };

        try {
            const response = await fetch(this.action, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify(data)
            });

            if (response.ok) {
                createCardModal.hide();
                showAlert('Card created successfully!', 'success');
                location.reload();
            } else {
                const errorData = await response.json();
                showAlert(errorData.message || 'Error creating card. Please try again.', 'danger');
            }
        } catch (error) {
            console.error('Error:', error);
            showAlert('Error creating card. Please try again.', 'danger');
        }
    });

    // Handle list deletion
    document.querySelectorAll('form[action*="/boardlists/delete/"]').forEach(form => {
        form.addEventListener('submit', async function(e) {
            e.preventDefault();
            
            if (confirm('Are you sure you want to delete this list? All cards in this list will also be deleted.')) {
                try {
                    const response = await fetch(this.action, {
                        method: 'POST'
                    });

                    if (response.ok) {
                        showAlert('List deleted successfully!', 'success');
                        this.closest('.list').remove();
                    } else {
                        showAlert('Error deleting list. Please try again.', 'danger');
                    }
                } catch (error) {
                    showAlert('Error deleting list. Please try again.', 'danger');
                    console.error('Error:', error);
                }
            }
        });
    });

    // Card detail handling
    window.openCardDetail = async function(cardElement) {
        const cardId = cardElement.getAttribute('data-card-id');
        console.log('Opening card details for ID:', cardId);
        currentCardId = cardId;
        
        try {
            console.log('Fetching card details from:', `/employee/cards/${cardId}`);
            const response = await fetch(`/employee/cards/${cardId}`);
            
            if (!response.ok) {
                console.error('Failed to fetch card details:', response.status, response.statusText);
                throw new Error('Failed to fetch card details');
            }
            
            const modalHtml = await response.text();
            console.log('Received modal HTML:', modalHtml);
            
            // Remove existing modal if it exists
            const existingModal = document.getElementById('cardDetailModal');
            if (existingModal) {
                existingModal.remove();
            }
            
            // Create new modal container
            const modalContainer = document.createElement('div');
            modalContainer.innerHTML = modalHtml;
            document.body.appendChild(modalContainer.firstElementChild);
            
            // Initialize the new modal
            cardDetailModal = new bootstrap.Modal(document.getElementById('cardDetailModal'));
            
            // Add event listeners after the modal is shown
            document.getElementById('cardDetailModal').addEventListener('shown.bs.modal', function() {
                document.getElementById('cardDetailForm').addEventListener('submit', handleCardUpdate);
                document.getElementById('commentForm').addEventListener('submit', handleCommentSubmit);
                document.getElementById('attachmentInput').addEventListener('change', handleFileUpload);
            });
            
            cardDetailModal.show();
            
        } catch (error) {
            console.error('Error in openCardDetail:', error);
            showAlert('Error loading card details', 'danger');
        }
    };

    async function handleCardUpdate(e) {
        e.preventDefault();
        
        const formData = new FormData(this);
        const data = {
            id: formData.get('id'),
            title: formData.get('title'),
            description: formData.get('description'),
            deadline: formData.get('deadline')
        };
        
        try {
            const response = await fetch(`/api/cards/${data.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify(data)
            });

            if (!response.ok) {
                throw new Error('Failed to update card');
            }
            
            showAlert('Card updated successfully', 'success');
            location.reload();
        } catch (error) {
            console.error('Error:', error);
            showAlert('Error updating card', 'danger');
        }
    }

    async function handleCommentSubmit(e) {
        e.preventDefault();
        
        const commentText = document.getElementById('commentText').value;
        if (!commentText.trim()) return;
        
        try {
            const response = await fetch(`/api/cards/${currentCardId}/comments`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify({ content: commentText })
            });

            if (!response.ok) {
                throw new Error('Failed to post comment');
            }
            
            document.getElementById('commentText').value = '';
            showAlert('Comment posted successfully', 'success');
            location.reload();
        } catch (error) {
            console.error('Error:', error);
            showAlert('Error posting comment', 'danger');
        }
    }

    async function handleFileUpload(e) {
        const file = e.target.files[0];
        if (!file) return;
        
        const formData = new FormData();
        formData.append('file', file);
        
        try {
            const response = await fetch(`/api/cards/${currentCardId}/attachments`, {
                method: 'POST',
                headers: {
                    [csrfHeader]: csrfToken
                },
                body: formData
            });

            if (!response.ok) {
                throw new Error('Failed to upload file');
            }
            
            showAlert('File uploaded successfully', 'success');
            location.reload();
        } catch (error) {
            console.error('Error:', error);
            showAlert('Error uploading file', 'danger');
        }
    }

    // Member management
    async function loadCardMembers(memberIds) {
        const membersList = document.getElementById('assignedMembersList');
        membersList.innerHTML = '';
        
        if (!memberIds || memberIds.length === 0) {
            membersList.innerHTML = '<p class="text-muted">No members assigned</p>';
            return;
        }
        
        try {
            const response = await fetch(`/employee/members?ids=${memberIds.join(',')}`);
            if (!response.ok) throw new Error('Failed to fetch members');
            
            const members = await response.json();
            members.forEach(member => {
                const memberDiv = document.createElement('div');
                memberDiv.className = 'member-item d-flex align-items-center mb-2';
                memberDiv.innerHTML = `
                    <span class="member-avatar me-2">${member.username.charAt(0).toUpperCase()}</span>
                    <span class="member-name">${member.username}</span>
                    <button class="btn btn-sm btn-danger ms-auto" onclick="removeMember(${member.id})">
                        <i class="fas fa-times"></i>
                    </button>
                `;
                membersList.appendChild(memberDiv);
            });
        } catch (error) {
            console.error('Error:', error);
            membersList.innerHTML = '<p class="text-danger">Error loading members</p>';
        }
    }

    window.showAddMemberModal = function() {
        const cardDetailModal = bootstrap.Modal.getInstance(document.getElementById('cardDetailModal'));
        const addMemberModal = new bootstrap.Modal(document.getElementById('addMemberModal'));
        
        // Store the current scroll position
        const scrollPosition = document.body.style.top;
        
        addMemberModal.show();
        
        // Event listener for when add member modal is hidden
        document.getElementById('addMemberModal').addEventListener('hidden.bs.modal', function() {
            // Restore the card detail modal's scroll position
            document.body.style.top = scrollPosition;
            cardDetailModal.show();
        }, { once: true });
    };

    window.addMemberToCard = async function() {
        const memberId = document.getElementById('memberSelect').value;
        if (!memberId) return;
        
        try {
            const response = await fetch(`/employee/cards/${currentCardId}/members`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify({ memberId })
            });

            if (!response.ok) throw new Error('Failed to add member');
            
            addMemberModal.hide();
            const card = await response.json();
            await loadCardMembers(card.assignedMemberIds);
            showAlert('Member added successfully', 'success');
        } catch (error) {
            console.error('Error:', error);
            showAlert('Error adding member', 'danger');
        }
    };

    window.removeMember = async function(memberId) {
        if (!confirm('Are you sure you want to remove this member?')) return;
        
        try {
            const response = await fetch(`/employee/cards/${currentCardId}/members/${memberId}`, {
                method: 'DELETE',
                headers: {
                    [csrfHeader]: csrfToken
                }
            });

            if (!response.ok) throw new Error('Failed to remove member');
            
            const card = await response.json();
            await loadCardMembers(card.assignedMemberIds);
            showAlert('Member removed successfully', 'success');
        } catch (error) {
            console.error('Error:', error);
            showAlert('Error removing member', 'danger');
        }
    };

    window.deleteCard = async function(cardId) {
        if (!confirm('Are you sure you want to delete this card? This action cannot be undone.')) {
            return;
        }
        
        try {
            const response = await fetch(`/employee/cards/${cardId}`, {
                method: 'DELETE',
                headers: {
                    [csrfHeader]: csrfToken
                }
            });

            if (!response.ok) {
                throw new Error('Failed to delete card');
            }
            
            // Close the modal and show success message
            const cardDetailModal = bootstrap.Modal.getInstance(document.getElementById('cardDetailModal'));
            cardDetailModal.hide();
            showAlert('Card deleted successfully', 'success');
            
            // Remove the card from the UI
            const cardElement = document.querySelector(`[data-card-id="${cardId}"]`);
            if (cardElement) {
                cardElement.remove();
            } else {
                location.reload();
            }
        } catch (error) {
            console.error('Error:', error);
            showAlert('Error deleting card', 'danger');
        }
    };

    // Alert function
    function showAlert(message, type) {
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type} alert-dismissible fade show position-fixed top-0 end-0 m-3`;
        alertDiv.role = 'alert';
        alertDiv.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        `;
        document.body.appendChild(alertDiv);

        setTimeout(() => {
            alertDiv.remove();
        }, 5000);
    }

    // Clear form inputs when modals are hidden
    document.getElementById('createCardModal').addEventListener('hidden.bs.modal', function() {
        document.getElementById('createCardForm').reset();
    });

    document.getElementById('createListModal').addEventListener('hidden.bs.modal', function() {
        document.getElementById('listTitle').value = '';
        document.getElementById('listDescription').value = '';
    });
}); 