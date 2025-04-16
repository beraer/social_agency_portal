document.addEventListener('DOMContentLoaded', function() {
    // File upload handling
    const fileInput = document.getElementById('fileInput');
    const fileUploadArea = document.querySelector('.file-upload');
    
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
    
    function updateFileName() {
        const fileName = fileInput.files[0]?.name;
        if (fileName) {
            fileUploadArea.querySelector('p').textContent = `Selected file: ${fileName}`;
        }
    }
    
    // Project status update
    const statusSelect = document.getElementById('projectStatus');
    if (statusSelect) {
        statusSelect.addEventListener('change', function() {
            const form = this.closest('form');
            if (form) {
                form.submit();
            }
        });
    }
    
    // Comment submission
    const commentForm = document.getElementById('commentForm');
    if (commentForm) {
        commentForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const commentText = this.querySelector('textarea').value;
            if (commentText.trim()) {
                // Here you would typically make an AJAX call to submit the comment
                console.log('Comment submitted:', commentText);
                this.reset();
            }
        });
    }
    
    // Initialize tooltips
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}); 