function showMessage(message, type = 'info') {
    const div = document.createElement('div');
    div.className = type + '-message';
    div.textContent = message;
    div.style.position = 'fixed';
    div.style.top = '20px';
    div.style.right = '20px';
    div.style.zIndex = '10000';
    div.style.minWidth = '300px';
    document.body.appendChild(div);
    setTimeout(() => div.remove(), 3000);
}

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('addStudentForm');
    if (form) {
        form.addEventListener('submit', handleAddStudent);
    }
});

async function handleAddStudent(e) {
    e.preventDefault();

    const data = {
        rollNumber: document.getElementById('rollNumber').value,
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
        email: document.getElementById('email').value,
        phone: document.getElementById('phone').value,
        course: document.getElementById('course').value,
        gpa: document.getElementById('gpa').value
    };

    try {
        const response = await fetch('/student-management/AddStudentServlet', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: new URLSearchParams(data)
        });

        if (response.ok) {
            showMessage('Student added!', 'success');
            document.getElementById('addStudentForm').reset();
            loadStudents();
        } else {
            showMessage('Failed to add student', 'error');
        }
    } catch (error) {
        showMessage('Error: ' + error.message, 'error');
    }
}
