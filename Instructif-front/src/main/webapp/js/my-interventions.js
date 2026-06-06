import { getStaff } from './localStorage-helper.js';

function formatGrade(n) {
    const grades = { 6: '6ème', 5: '5ème', 4: '4ème', 3: '3ème', 2: '2nde', 1: '1ère', 0: 'Terminale' };
    return grades[n] ?? n + 'ème';
}

async function fetchMyInterventions() {
    try {
        const response = await fetch('ActionServlet?todo=my-interventions');
        const data = await response.json();
        console.log('[my-interventions] réponse serveur :', data);
        console.log('[my-interventions] interventions :', data?.interventions);
        console.log('[my-interventions] affectation courante :', data?.currentAssignment);
        return data;
    } catch (error) {
        console.error('[my-interventions] erreur fetch :', error);
        return null;
    }
}

function renderProfile(staff) {
    document.getElementById('profile-name').textContent = `${staff.lastName.toUpperCase()} ${staff.firstName}`;
    document.getElementById('profile-login').textContent = staff.login;
    document.getElementById('profile-phone').textContent = staff.phone || '—';
    document.getElementById('profile-grades').textContent = `${formatGrade(staff.minSchoolGrade)} → ${formatGrade(staff.maxSchoolGrade)}`;
    document.getElementById('sidebar-initials').textContent = (staff.firstName[0] + staff.lastName[0]).toUpperCase();
    document.getElementById('sidebar-name').textContent = `${staff.firstName} ${staff.lastName}`;

    const extra = document.getElementById('profile-extra');
    if (staff.type === 'STUDENT') {
        extra.innerHTML = `
            <div class="profile-item"><label>Université</label><div>${staff.university || '—'}</div></div>
            <div class="profile-item"><label>Spécialité</label><div>${staff.speciality || '—'}</div></div>`;
    } else if (staff.type === 'TEACHER') {
        extra.innerHTML = `
            <div class="profile-item"><label>Type d'établissement</label><div>${staff.schoolType || '—'}</div></div>`;
    } else if (staff.type === 'OTHER') {
        extra.innerHTML = `
            <div class="profile-item"><label>Activité</label><div>${staff.activity || '—'}</div></div>`;
    }
}

function renderCurrentAssignment(assignment) {
    const block = document.getElementById('assignment-block');
    if (!assignment) {
        block.style.display = 'none';
        return;
    }
    document.getElementById('assignment-student').textContent =
        `${assignment.studentFirstName} ${assignment.studentLastName} (${formatGrade(assignment.studentGrade)})`;
    document.getElementById('assignment-topic').textContent =
        `${assignment.subject} · ${assignment.topic}`;
    const joinBtn = document.getElementById('assignment-btn');
    if (assignment.meetingLink) joinBtn.href = assignment.meetingLink;
}

function renderTable(interventions) {
    const tbody = document.getElementById('interventions-tbody');
    if (!interventions || interventions.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" style="text-align:center;color:var(--text-muted);padding:2rem">Aucune intervention pour le moment.</td></tr>';
        return;
    }
    tbody.innerHTML = interventions.map(d => {
        let statusBadge;
        if (d.status === 'TERMINEE') {
            statusBadge = '<span class="badge badge-success">TERMINÉE</span>';
        } else if (d.status === 'ANNULEE') {
            statusBadge = '<span class="badge badge-danger">ANNULÉE</span>';
        } else {
            statusBadge = '<span class="badge badge-warning">EN COURS</span>';
        }
        const bilanCell = d.status === 'TERMINEE'
            ? `<a href="detail-demande-bilan.html?id=${d.id}" class="link-action">Voir le bilan ›</a>`
            : '—';
        return `<tr>
            <td>${d.startDate}</td>
            <td>${d.endDate || '—'}</td>
            <td>${d.studentFirstName} ${d.studentLastName} (${formatGrade(d.studentGrade)})</td>
            <td>${d.subject} › ${d.topic}</td>
            <td>${statusBadge}</td>
            <td>${bilanCell}</td>
        </tr>`;
    }).join('');
}

async function init() {
    const staff = getStaff();
    if (staff) renderProfile(staff);

    const data = await fetchMyInterventions();
    if (!data) return;

    renderCurrentAssignment(data.currentAssignment);
    renderTable(data.interventions);
}

window.addEventListener('load', init);
