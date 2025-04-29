// Card Operations (Drag & Drop, Reordering)
class CardOperations {
    constructor() {
        this.setupDragAndDrop();
    }

    setupDragAndDrop() {
        const boardLists = document.querySelector('.board-lists');
        if (boardLists) {
            new Sortable(boardLists, {
                animation: 150,
                draggable: '.list',
                handle: '.list-header',
                ghostClass: 'list-ghost',
                chosenClass: 'list-chosen',
                dragClass: 'list-drag'
            });
        }

        const lists = document.querySelectorAll('.list-cards');
        lists.forEach(list => {
            new Sortable(list, {
                group: 'cards',
                animation: 150,
                draggable: '.card',
                filter: '.card-placeholder',
                ghostClass: 'card-ghost',
                chosenClass: 'card-chosen',
                dragClass: 'card-drag',
                onEnd: (evt) => this.handleCardMove(evt)
            });
        });
    }

    async handleCardMove(evt) {
        const cardId = evt.item.getAttribute('data-card-id');
        const newListId = evt.to.closest('.list').getAttribute('data-list-id');
        const cardIds = Array.from(evt.to.children)
            .filter(el => el.classList.contains('card'))
            .map(card => card.getAttribute('data-card-id'));

        try {
            const csrfToken = document.querySelector('meta[name="_csrf"]').content;
            const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

            const response = await fetch(`/employee/cards/${cardId}/move`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify({
                    newListId: newListId,
                    cardIds: cardIds
                })
            });

            if (!response.ok) {
                evt.item.parentNode.removeChild(evt.item);
                evt.from.insertBefore(evt.item, evt.from.children[evt.oldIndex]);
                throw new Error('Failed to move card');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Error moving card: ' + error.message);
            evt.item.parentNode.removeChild(evt.item);
            evt.from.insertBefore(evt.item, evt.from.children[evt.oldIndex]);
        }
    }
}

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    window.cardOperations = new CardOperations();
}); 