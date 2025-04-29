document.addEventListener('DOMContentLoaded', function() {
    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

    const listsContainer = document.getElementById('lists-container');
    if (listsContainer) {
        new Sortable(listsContainer, {
            animation: 150,
            handle: '.list-header',
            onEnd: (evt) => handleListReorder(evt)
        });
    }

    document.querySelectorAll('.list-cards').forEach(listCards => {
        new Sortable(listCards, {
            animation: 150,
            group: 'cards',
            onEnd: (evt) => handleCardReorder(evt),
            filter: '.card-placeholder',
            preventOnFilter: true
        });
    });

    async function handleListReorder(evt) {
        const lists = Array.from(evt.to.children);
        const listIds = lists.map(list => list.getAttribute('data-list-id')).filter(id => id);

        try {
            const response = await fetch('/employee/boardlists/reorder', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify({ listIds: listIds })
            });

            if (!response.ok) {
                throw new Error('Failed to update list order');
            }
        } catch (error) {
            console.error('Error updating list order:', error);
            evt.item.parentNode.insertBefore(evt.item, evt.oldIndex < evt.newIndex ? evt.oldIndex : evt.oldIndex + 1);
        }
    }

    async function handleCardReorder(evt) {
        if (!evt.to) return;

        const listCards = evt.to;
        const listId = listCards.closest('.list').getAttribute('data-list-id');
        const cards = Array.from(listCards.querySelectorAll('.card'));
        const cardIds = cards.map(card => card.getAttribute('data-card-id')).filter(id => id);

        try {
            const response = await fetch('/employee/cards/reorder', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify({
                    listId: listId,
                    cardIds: cardIds
                })
            });

            if (!response.ok) {
                throw new Error('Failed to update card order');
            }
        } catch (error) {
            console.error('Error updating card order:', error);
            evt.item.parentNode.insertBefore(evt.item, evt.oldIndex < evt.newIndex ? evt.oldIndex : evt.oldIndex + 1);
        }
    }

    const createCardModal = document.getElementById('createCardModal');
    if (createCardModal) {
        createCardModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const listId = button.getAttribute('data-list-id');
            const listIdInput = this.querySelector('input[name="listId"]');
            if (listIdInput) {
                listIdInput.value = listId;
            }
        });
    }

    const cardForm = document.getElementById('cardForm');
    if (cardForm) {
        cardForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            const formData = new FormData(this);
            const data = Object.fromEntries(formData.entries());

            try {
                const response = await fetch(this.action, {
                    method: this.method,
                    headers: {
                        'Content-Type': 'application/json',
                        [csrfHeader]: csrfToken
                    },
                    body: JSON.stringify(data)
                });

                if (!response.ok) {
                    throw new Error('Failed to create card');
                }

                window.location.reload();
            } catch (error) {
                console.error('Error:', error);
                alert('Error creating card: ' + error.message);
            }
        });
    }

    window.deleteList = async function(listId) {
        if (confirm('Are you sure you want to delete this list and all its cards?')) {
            try {
                const response = await fetch(`/employee/boardlists/${listId}/delete`, {
                    method: 'POST',
                    headers: {
                        [csrfHeader]: csrfToken
                    }
                });

                if (!response.ok) {
                    throw new Error('Failed to delete list');
                }

                showAlert('List deleted successfully', 'success');
                location.reload();
            } catch (error) {
                console.error('Error:', error);
                showAlert('Error deleting list', 'danger');
            }
        }
    };

    window.openCardDetail = async function(cardId) {
        try {
            const response = await fetch(`/employee/cards/${cardId}`);
            if (!response.ok) {
                throw new Error('Failed to fetch card details');
            }

            const html = await response.text();
            const modalContainer = document.getElementById('modalContainer');
            if (!modalContainer) {
                throw new Error('Modal container not found');
            }

            modalContainer.innerHTML = html;
            const modal = new bootstrap.Modal(document.getElementById('cardDetailModal'));
            modal.show();
        } catch (error) {
            console.error('Error:', error);
            alert('Error opening card: ' + error.message);
        }
    };

    document.querySelectorAll('.card').forEach(card => {
        card.addEventListener('click', function(e) {
            if (!e.target.closest('.card-actions')) {
                const cardId = this.getAttribute('data-card-id');
                if (cardId) {
                    openCardDetail(cardId);
                }
            }
        });
    });

    window.deleteCard = async function(cardId) {
        if (confirm('Are you sure you want to delete this card?')) {
            try {
                const response = await fetch(`/employee/cards/${cardId}/delete`, {
                    method: 'POST',
                    headers: {
                        [csrfHeader]: csrfToken
                    }
                });

                if (!response.ok) {
                    throw new Error('Failed to delete card');
                }

                showAlert('Card deleted successfully', 'success');
                location.reload();
            } catch (error) {
                console.error('Error:', error);
                showAlert('Error deleting card', 'danger');
            }
        }
    };

    window.addMemberToCard = async function(cardId, memberId) {
        try {
            const response = await fetch(`/employee/cards/${cardId}/members/add`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify({ memberId: memberId })
            });

            if (!response.ok) {
                throw new Error('Failed to add member');
            }

            showAlert('Member added successfully', 'success');
            location.reload();
        } catch (error) {
            console.error('Error:', error);
            showAlert('Error adding member', 'danger');
        }
    };

    window.removeMemberFromCard = async function(cardId, memberId) {
        try {
            const response = await fetch(`/employee/cards/${cardId}/members/remove`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify({ memberId: memberId })
            });

            if (!response.ok) {
                throw new Error('Failed to remove member');
            }

            showAlert('Member removed successfully', 'success');
            location.reload();
        } catch (error) {
            console.error('Error:', error);
            showAlert('Error removing member', 'danger');
        }
    };

    function showAlert(message, type = 'success') {
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

    window.openCardDetail = openCardDetail;
}); 