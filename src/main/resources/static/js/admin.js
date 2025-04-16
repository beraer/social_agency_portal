function editUser(button) {
    const userId = button.getAttribute('data-id');
    // Implement edit functionality
    console.log('Edit user:', userId);
}

function deleteUser(button) {
    const userId = button.getAttribute('data-id');
    if (confirm('Are you sure you want to delete this user?')) {
        fetch(`/admin/users/${userId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                button.closest('tr').remove();
                showAlert('User deleted successfully', 'success');
            } else {
                showAlert('Error deleting user', 'danger');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showAlert('Error deleting user', 'danger');
        });
    }
}

function promoteToAdmin(button) {
    const userId = button.getAttribute('data-id');
    if (confirm('Are you sure you want to promote this user to admin?')) {
        fetch(`/admin/users/${userId}/promote`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                location.reload();
                showAlert('User promoted to admin successfully', 'success');
            } else {
                showAlert('Error promoting user', 'danger');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showAlert('Error promoting user', 'danger');
        });
    }
}

function showAlert(message, type) {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
    alertDiv.role = 'alert';
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    const container = document.querySelector('.container');
    container.insertBefore(alertDiv, container.firstChild);
    
    setTimeout(() => {
        alertDiv.remove();
    }, 5000);
}

// Handle form submissions
document.addEventListener('DOMContentLoaded', function() {
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            if (!form.checkValidity()) {
                e.preventDefault();
                e.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });
}); 