document.addEventListener('DOMContentLoaded', () => {
    loadStudents();
});

async function loadStudents() {
    const container = document.getElementById('tableContainer');
    container.innerHTML = '<p class="loading">Loading...</p>';

    try {
        const response = await fetch('/student-management/ViewStudentsServlet');
        const data = await response.json();

        if (data.students && data.students.length > 0) {
            displayStudents(data.students);
        } else {
            container.innerHTML = '<p class="loading">No students found</p>';
        }
    } catch (error) {
        container.innerHTML = '<p class="loading">Error loading students</p>';
    }
}

function displayStudents(students) {
    let html = `
        <table>
            <thead>
                <tr>
                    <th>Roll #</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Course</th>
                    <th>GPA</th>
                </tr>
            </thead>
            <tbody>
    `;

    students.forEach(s => {
        html += `
            <tr>
                <td>${escape(s.rollNumber)}</td>
                <td>${escape(s.firstName)} ${escape(s.lastName)}</td>
                <td>${escape(s.email)}</td>
                <td>${escape(s.phone)}</td>
                <td>${escape(s.course)}</td>
                <td>${s.gpa}</td>
            </tr>
        `;
    });

    html += '</tbody></table>';
    document.getElementById('tableContainer').innerHTML = html;
}

function escape(str) {
    const div = document.createElement('div');
    div.textContent = str;
    return div.innerHTML;
}
