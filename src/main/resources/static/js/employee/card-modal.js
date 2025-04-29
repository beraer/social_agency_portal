// Card Modal Management
class CardModal {
    constructor() {
        this.modal = null;
        this.modalElement = null;
        console.log('[CardModal] Initializing...');
        this.setupEventListeners();
    }

    setupEventListeners() {
        console.log('[CardModal] Setting up event listeners...');
        
        // Use event delegation for card clicks
        document.addEventListener('click', (e) => {
            const card = e.target.closest('.card');
            if (card) {
                console.log('[CardModal] Card clicked:', card);
                e.preventDefault();
                const cardId = card.getAttribute('data-card-id');
                console.log('[CardModal] Card ID:', cardId);
                if (cardId) {
                    this.openCardDetail(cardId);
                }
            }
        });

        // Use event delegation for add card button clicks
        document.addEventListener('click', (e) => {
            const addCardBtn = e.target.closest('.add-card-btn');
            if (addCardBtn) {
                console.log('[CardModal] Add card button clicked');
                e.preventDefault();
                const listId = addCardBtn.getAttribute('data-list-id');
                console.log('[CardModal] List ID:', listId);
                if (listId) {
                    this.openCardDetail(null, listId);
                }
            }
        });

        // Event delegation for member management and other interactions
        document.addEventListener('click', async (e) => {
            if (!this.modalElement) return;

            // Title editing
            if (e.target.closest('.card-title-display')) {
                this.toggleTitleEdit(true);
            }

            // Description editing
            if (e.target.closest('.edit-description-btn')) {
                this.toggleDescriptionEdit(true);
            }
            if (e.target.closest('.save-description-btn')) {
                await this.saveDescription();
            }
            if (e.target.closest('.cancel-description-btn')) {
                this.toggleDescriptionEdit(false);
            }

            // Member management
            if (e.target.closest('.add-member-item')) {
                const item = e.target.closest('.add-member-item');
                const memberId = item.getAttribute('data-member-id');
                const memberName = item.getAttribute('data-member-name');
                await this.handleAddMember(memberId, memberName);
            }
            if (e.target.closest('.remove-member-btn')) {
                const btn = e.target.closest('.remove-member-btn');
                await this.handleRemoveMember(btn);
            }

            // Labels toggle
            if (e.target.closest('.btn-labels')) {
                this.toggleLabelsContainer();
            }
        });

        // Handle title edit save on enter
        document.addEventListener('keydown', (e) => {
            if (!this.modalElement) return;
            
            if (e.key === 'Enter' && e.target.classList.contains('card-title-edit')) {
                e.preventDefault();
                this.saveTitle();
            }
        });

        // Handle description edit save on ctrl+enter
        document.addEventListener('keydown', (e) => {
            if (!this.modalElement) return;
            
            if (e.key === 'Enter' && e.ctrlKey && e.target.id === 'description') {
                e.preventDefault();
                this.saveDescription();
            }
        });
    }

    toggleTitleEdit(show) {
        const titleDisplay = this.modalElement.querySelector('.card-title-display');
        const titleEdit = this.modalElement.querySelector('.card-title-edit');
        
        if (show) {
            titleDisplay.classList.add('d-none');
            titleEdit.classList.remove('d-none');
            titleEdit.value = titleDisplay.textContent.trim();
            titleEdit.focus();
        } else {
            titleDisplay.classList.remove('d-none');
            titleEdit.classList.add('d-none');
        }
    }

    async saveTitle() {
        const titleDisplay = this.modalElement.querySelector('.card-title-display');
        const titleEdit = this.modalElement.querySelector('.card-title-edit');
        const cardId = this.modalElement.querySelector('input[name="id"]')?.value;
        
        if (!cardId) {
            titleDisplay.textContent = titleEdit.value;
            this.toggleTitleEdit(false);
            return;
        }

        try {
            const csrfToken = document.querySelector('meta[name="_csrf"]').content;
            const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

            const response = await fetch(`/employee/cards/${cardId}/title`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify({ title: titleEdit.value })
            });

            if (!response.ok) throw new Error('Failed to update title');

            titleDisplay.textContent = titleEdit.value;
            this.toggleTitleEdit(false);
        } catch (error) {
            console.error('[CardModal] Error updating title:', error);
            alert('Error updating title: ' + error.message);
        }
    }

    toggleDescriptionEdit(show) {
        const descDisplay = this.modalElement.querySelector('.description-display');
        const descEdit = this.modalElement.querySelector('.description-edit');
        
        if (show) {
            descDisplay.classList.add('d-none');
            descEdit.classList.remove('d-none');
            descEdit.querySelector('textarea').focus();
        } else {
            descDisplay.classList.remove('d-none');
            descEdit.classList.add('d-none');
        }
    }

    async saveDescription() {
        const descDisplay = this.modalElement.querySelector('.description-display');
        const textarea = this.modalElement.querySelector('#description');
        const cardId = this.modalElement.querySelector('input[name="id"]')?.value;
        
        if (!cardId) {
            descDisplay.textContent = textarea.value || 'Add a more detailed description...';
            this.toggleDescriptionEdit(false);
            return;
        }

        try {
            const csrfToken = document.querySelector('meta[name="_csrf"]').content;
            const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

            const response = await fetch(`/employee/cards/${cardId}/description`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify({ description: textarea.value })
            });

            if (!response.ok) throw new Error('Failed to update description');

            descDisplay.textContent = textarea.value || 'Add a more detailed description...';
            this.toggleDescriptionEdit(false);
        } catch (error) {
            console.error('[CardModal] Error updating description:', error);
            alert('Error updating description: ' + error.message);
        }
    }

    async handleAddMember(memberId, memberName) {
        if (!memberId) return;

        const cardId = this.modalElement.querySelector('input[name="id"]')?.value;
        if (!cardId) {
            // For new cards, just add to the UI
            this.addMemberToUI(memberId, memberName);
            return;
        }

        try {
            const csrfToken = document.querySelector('meta[name="_csrf"]').content;
            const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

            const response = await fetch(`/employee/cards/${cardId}/members/add`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify({ memberId: memberId })
            });

            if (!response.ok) throw new Error('Failed to add member');

            this.addMemberToUI(memberId, memberName);
        } catch (error) {
            console.error('[CardModal] Error adding member:', error);
            alert('Error adding member: ' + error.message);
        }
    }

    async handleRemoveMember(button) {
        const memberId = button.getAttribute('data-member-id');
        const memberBadge = button.closest('.member-badge');

        const cardId = this.modalElement.querySelector('input[name="id"]')?.value;
        if (!cardId) {
            // For new cards, just remove from UI
            memberBadge.remove();
            this.updateMemberIds();
            return;
        }

        try {
            const csrfToken = document.querySelector('meta[name="_csrf"]').content;
            const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

            const response = await fetch(`/employee/cards/${cardId}/members/remove`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify({ memberId: memberId })
            });

            if (!response.ok) throw new Error('Failed to remove member');

            memberBadge.remove();
            this.updateMemberIds();
        } catch (error) {
            console.error('[CardModal] Error removing member:', error);
            alert('Error removing member: ' + error.message);
        }
    }

    addMemberToUI(memberId, memberName) {
        const membersList = this.modalElement.querySelector('#membersList');
        
        // Check if member already exists
        if (membersList.querySelector(`[data-member-id="${memberId}"]`)) {
            return;
        }

        const memberBadge = document.createElement('div');
        memberBadge.className = 'member-badge';
        memberBadge.innerHTML = `
            <div class="avatar-circle" title="${memberName}">
                ${memberName.charAt(0).toUpperCase()}
            </div>
            <button type="button" class="remove-member-btn" 
                    data-member-id="${memberId}"
                    title="Remove member">×</button>
        `;
        membersList.appendChild(memberBadge);

        this.updateMemberIds();
    }

    updateMemberIds() {
        const membersList = this.modalElement.querySelector('#membersList');
        const members = membersList.querySelectorAll('.member-badge');
        const memberIds = Array.from(members).map(badge => 
            badge.querySelector('.remove-member-btn').getAttribute('data-member-id')
        );

        // Update or create hidden input
        let hiddenInput = membersList.querySelector('input[name="assignedMemberIds"]');
        if (!hiddenInput) {
            hiddenInput = document.createElement('input');
            hiddenInput.type = 'hidden';
            hiddenInput.name = 'assignedMemberIds';
            membersList.insertBefore(hiddenInput, membersList.firstChild);
        }
        hiddenInput.value = memberIds.join(',');
    }

    toggleLabelsContainer() {
        const container = this.modalElement.querySelector('.labels-container');
        container.classList.toggle('d-none');
    }

    async openCardDetail(cardId, listId = null) {
        console.log('[CardModal] Opening card detail...', { cardId, listId });
        try {
            let url = cardId ? `/employee/cards/${cardId}` : `/employee/cards/new?listId=${listId}`;
            console.log('[CardModal] Fetching from URL:', url);
            
            const response = await fetch(url);
            console.log('[CardModal] Response status:', response.status);
            
            if (!response.ok) {
                throw new Error(`Failed to fetch card details. Status: ${response.status}`);
            }

            const html = await response.text();
            console.log('[CardModal] Received HTML length:', html.length);

            const modalContainer = document.getElementById('modalContainer');
            console.log('[CardModal] Modal container found:', !!modalContainer);
            if (!modalContainer) {
                throw new Error('Modal container not found - Element with ID "modalContainer" is missing');
            }

            // Clean up any existing modal
            if (this.modal) {
                console.log('[CardModal] Cleaning up existing modal');
                this.modal.dispose();
                this.modal = null;
            }

            console.log('[CardModal] Setting modal container HTML');
            modalContainer.innerHTML = html;

            // Initialize the modal
            this.modalElement = document.getElementById('cardDetailModal');
            console.log('[CardModal] Card detail modal element found:', !!this.modalElement);
            if (!this.modalElement) {
                throw new Error('Card detail modal element not found - Element with ID "cardDetailModal" is missing in the received HTML');
            }

            // Set up form submission
            const form = this.modalElement.querySelector('#cardForm');
            if (form) {
                form.onsubmit = async (e) => {
                    e.preventDefault();
                    await this.handleFormSubmit(form);
                };
            }

            console.log('[CardModal] Creating Bootstrap modal instance');
            this.modal = new bootstrap.Modal(this.modalElement);
            
            console.log('[CardModal] Showing modal');
            this.modal.show();

            // Dispatch cardModalOpened event
            console.log('[CardModal] Dispatching cardModalOpened event');
            this.modalElement.dispatchEvent(new CustomEvent('cardModalOpened', {
                bubbles: true,
                detail: { cardId: cardId }
            }));

        } catch (error) {
            console.error('[CardModal] Error in openCardDetail:', error);
            console.error('[CardModal] Error stack:', error.stack);
            alert('Error opening card detail: ' + error.message);
        }
    }

    async handleFormSubmit(form) {
        try {
            const formData = new FormData(form);
            const data = Object.fromEntries(formData.entries());
            
            // Convert assignedMemberIds from comma-separated string to array
            if (data.assignedMemberIds) {
                data.assignedMemberIds = data.assignedMemberIds.split(',').map(id => parseInt(id));
            } else {
                data.assignedMemberIds = [];
            }
            
            const csrfToken = document.querySelector('meta[name="_csrf"]').content;
            const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;
            
            const url = form.action;
            const method = form.method;
            
            const response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify(data)
            });

            if (!response.ok) {
                throw new Error('Failed to save card');
            }

            // Close the modal
            this.modal.hide();
            
            // Refresh the page to show updated card
            window.location.reload();
        } catch (error) {
            console.error('[CardModal] Error submitting form:', error);
            alert('Error saving card: ' + error.message);
        }
    }

    // Clean up method
    dispose() {
        console.log('[CardModal] Disposing modal');
        if (this.modal) {
            this.modal.dispose();
            this.modal = null;
        }
        this.modalElement = null;
    }
}

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    console.log('[CardModal] DOM loaded, initializing CardModal');
    window.cardModal = new CardModal();
});

// Clean up on page unload
window.addEventListener('unload', () => {
    console.log('[CardModal] Page unloading, cleaning up');
    if (window.cardModal) {
        window.cardModal.dispose();
    }
}); 