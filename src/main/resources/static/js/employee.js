let currentProjectId = null;

function uploadFile(button) {
    currentProjectId = button.getAttribute('data-id');
    const modal = new bootstrap.Modal(document.getElementById('uploadModal'));
    modal.show();
}

function viewFiles(button) {
    const projectId = button.getAttribute('data-id');
    window.location.href = `/employee/projects/${projectId}/files`;
}

function updateStatus(select) {
    const projectId = select.getAttribute('data-id');
    const newStatus = select.value;
    
    fetch(`/employee/projects/${projectId}/status`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ status: newStatus })
    })
    .then(response => {
        if (response.ok) {
            showAlert('Status updated successfully', 'success');
        } else {
            showAlert('Error updating status', 'danger');
            // Revert the select value
            select.value = select.getAttribute('data-original-value');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showAlert('Error updating status', 'danger');
        // Revert the select value
        select.value = select.getAttribute('data-original-value');
    });
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

// File upload handling
document.addEventListener('DOMContentLoaded', function() {
    const fileInput = document.getElementById('fileInput');
    const fileUploadArea = document.querySelector('.file-upload');
    const uploadForm = document.getElementById('uploadForm');
    
    if (fileUploadArea) {
        fileUploadArea.addEventListener('click', () => fileInput.click());
        
        fileUploadArea.addEventListener('dragover', (e) => {
            e.preventDefault();
            fileUploadArea.classList.add('border-primary');
        });
        
        fileUploadArea.addEventListener('dragleave', () => {
            fileUploadArea.classList.remove('border-primary');
        });
        
        fileUploadArea.addEventListener('drop', (e) => {
            e.preventDefault();
            fileUploadArea.classList.remove('border-primary');
            fileInput.files = e.dataTransfer.files;
            updateFileName();
        });
        
        fileInput.addEventListener('change', updateFileName);
    }
    
    if (uploadForm) {
        uploadForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            const formData = new FormData();
            formData.append('file', fileInput.files[0]);
            formData.append('projectId', currentProjectId);
            formData.append('fileType', this.querySelector('[name="fileType"]').value);
            
            fetch('/employee/upload', {
                method: 'POST',
                body: formData
            })
            .then(response => {
                if (response.ok) {
                    showAlert('File uploaded successfully', 'success');
                    bootstrap.Modal.getInstance(document.getElementById('uploadModal')).hide();
                    location.reload();
                } else {
                    showAlert('Error uploading file', 'danger');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showAlert('Error uploading file', 'danger');
            });
        });
    }
    
    function updateFileName() {
        const fileName = fileInput.files[0]?.name;
        if (fileName) {
            fileUploadArea.querySelector('p').textContent = `Selected file: ${fileName}`;
        }
    }
    
    // Store original status values
    document.querySelectorAll('select[onchange="updateStatus(this)"]').forEach(select => {
        select.setAttribute('data-original-value', select.value);
    });
}); 