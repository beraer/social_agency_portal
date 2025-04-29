// Common Utilities
const utils = {
    formatDate: function(date) {
        if (!date) return '';
        const d = new Date(date);
        return d.toLocaleDateString() + ' ' + d.toLocaleTimeString();
    },

    showAlert: function(message, type = 'success', duration = 5000) {
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
        }, duration);
    },

    addCsrfToHeaders: function(headers = {}) {
        const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;
        
        if (csrfToken && csrfHeader) {
            return {
                ...headers,
                [csrfHeader]: csrfToken
            };
        }
        return headers;
    },

    handleApiError: function(error, defaultMessage = 'An error occurred') {
        console.error('API Error:', error);
        this.showAlert(error.message || defaultMessage, 'danger');
    }
};

// Make utils globally available
window.utils = utils; 