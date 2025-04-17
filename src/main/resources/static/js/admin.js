document.addEventListener('DOMContentLoaded', function () {
    showSection('projectSection');
    initializeTooltips();
});

function showSection(id) {
    ['projectSection', 'employeeSection', 'clientSection'].forEach(sec => {
        document.getElementById(sec).style.display = (sec === id) ? 'block' : 'none';
    });
}

function deleteUser(userId) {
    if (!confirm('Are you sure you want to delete this user?')) return;

    fetch(`/admin/users/${userId}`, {
        method: 'DELETE'
    }).then(response => {
        if (!response.ok) throw new Error('Failed to delete user');
        location.reload();
    }).catch(error => {
        console.error('Error:', error);
    });
}

function promoteToAdmin(userId) {
    if (!confirm('Promote to admin?')) return;

    fetch(`/admin/users/${userId}/promote`, {
        method: 'POST'
    }).then(response => {
        if (!response.ok) throw new Error('Failed to promote user');
        location.reload();
    }).catch(error => {
        console.error('Error:', error);
    });
}

function initializeTooltips() {
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}


function editUser(userId) {
    fetch(`/admin/users/${userId}`)
        .then(res => res.json())
        .then(user => {
            document.getElementById('editUserId').value = user.id;
            document.getElementById('editUsername').value = user.username;
            document.getElementById('editRole').value = user.role;

            new bootstrap.Modal(document.getElementById('editUserModal')).show();
        });
}

document.getElementById('editUserForm').addEventListener('submit', function (e) {
    e.preventDefault();

    const userId = document.getElementById('editUserId').value;
    const payload = {
        username: document.getElementById('editUsername').value,
        role: document.getElementById('editRole').value
    };

    fetch(`/admin/users/${userId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    })
        .then(res => {
            if (!res.ok) throw new Error("Update failed");
            return res.json();
        })
        .then(() => location.reload())
        .catch(err => console.error(err));
});
