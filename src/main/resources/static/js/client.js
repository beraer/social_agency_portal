let currentProjectId = null;

function viewProject(button) {
    currentProjectId = button.getAttribute('data-project-id');
    const modal = document.getElementById('projectDetailsModal');
    const projectTitle = button.closest('tr').querySelector('td:first-child').textContent;
    
    // Update modal title
    document.getElementById('projectModalLabel').textContent = projectTitle;
    
    // Load project comments
    loadComments();
    
    // Show modal
    const bsModal = new bootstrap.Modal(modal);
    bsModal.show();
}

function loadComments() {
    if (!currentProjectId) return;
    
    fetch(`/api/comments/project/${currentProjectId}`)
        .then(response => response.json())
        .then(comments => {
            const commentsContainer = document.getElementById('projectComments');
            commentsContainer.innerHTML = '';
            
            comments.forEach(comment => {
                const commentElement = document.createElement('div');
                commentElement.className = 'comment mb-3 p-3 border rounded';
                commentElement.innerHTML = `
                    <div class="d-flex justify-content-between">
                        <strong>${comment.authorName}</strong>
                        <small class="text-muted">${new Date(comment.createdAt).toLocaleString()}</small>
                    </div>
                    <p class="mb-0 mt-2">${comment.content}</p>
                `;
                commentsContainer.appendChild(commentElement);
            });
        })
        .catch(error => {
            console.error('Error loading comments:', error);
            showAlert('Failed to load comments', 'danger');
        });
}

document.getElementById('commentForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const commentContent = document.getElementById('commentContent').value.trim();
    if (!commentContent) {
        showAlert('Please enter a comment', 'warning');
        return;
    }
    
    const payload = {
        projectId: currentProjectId,
        content: commentContent
    };
    
    fetch('/api/comments', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload)
    })
    .then(response => {
        if (!response.ok) throw new Error('Failed to post comment');
        return response.json();
    })
    .then(() => {
        document.getElementById('commentContent').value = '';
        loadComments();
        showAlert('Comment posted successfully', 'success');
    })
    .catch(error => {
        console.error('Error posting comment:', error);
        showAlert('Failed to post comment', 'danger');
    });
});

function showAlert(message, type) {
    const alertPlaceholder = document.getElementById('alertPlaceholder');
    const wrapper = document.createElement('div');
    wrapper.innerHTML = `
        <div class="alert alert-${type} alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    `;
    alertPlaceholder.appendChild(wrapper);
    
    // Auto-dismiss after 5 seconds
    setTimeout(() => {
        const alert = wrapper.querySelector('.alert');
        if (alert) {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }
    }, 5000);
}

function updateProjectStatus(select) {
    const projectId = select.getAttribute('data-project-id');
    const newStatus = select.value;
    
    fetch(`/api/projects/${projectId}/status`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ status: newStatus })
    })
    .then(response => {
        if (!response.ok) throw new Error('Failed to update status');
        showAlert('Project status updated successfully', 'success');
    })
    .catch(error => {
        console.error('Error updating project status:', error);
        showAlert('Failed to update project status', 'danger');
        // Reset select to previous value
        select.value = select.getAttribute('data-original-status');
    });
}

document.addEventListener('DOMContentLoaded', function() {
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function(tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}); 