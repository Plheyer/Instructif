import { getStaff } from './localStorage-helper.js';
import { formatGrade } from './format.js';

async function fetchStats() {
    try {
        const res = await fetch('ActionServlet?todo=statistiques');
        return await res.json();
    } catch (err) {
        console.error('[statistiques] erreur fetch stats :', err);
        return null;
    }
}

async function fetchInterventions() {
    try {
        const res = await fetch('ActionServlet?todo=my-interventions');
        return await res.json();
    } catch (err) {
        console.error('[statistiques] erreur fetch interventions :', err);
        return null;
    }
}

function renderSidebar(staff, currentAssignment) {
    if (staff) {
        document.getElementById('sidebar-initials').textContent =
            (staff.firstName[0] + staff.lastName[0]).toUpperCase();
        document.getElementById('sidebar-name').textContent =
            `${staff.firstName} ${staff.lastName}`;
    }
    const block = document.getElementById('assignment-block');
    if (!currentAssignment) { block.style.display = 'none'; return; }
    document.getElementById('assignment-student').textContent =
        `${currentAssignment.studentFirstName} ${currentAssignment.studentLastName} (${formatGrade(currentAssignment.studentGrade)})`;
    document.getElementById('assignment-topic').textContent =
        `${currentAssignment.subject} · ${currentAssignment.topic}`;
    const btn = document.getElementById('assignment-btn');
    if (currentAssignment.meetingLink) btn.href = currentAssignment.meetingLink;
}

function renderTable(tbodyId, rows) {
    const tbody = document.getElementById(tbodyId);
    if (!rows || rows.length === 0) {
        tbody.innerHTML = '<tr><td colspan="2" style="text-align:center;color:var(--text-muted);padding:1rem">Aucune donnée</td></tr>';
        return;
    }
    tbody.innerHTML = rows.map(r =>
        `<tr><td>${r.label}</td><td class="value">${r.count}</td></tr>`
    ).join('');
}

async function init() {
    const staff = getStaff();
    const [stats, interventionsData] = await Promise.all([fetchStats(), fetchInterventions()]);

    renderSidebar(staff, interventionsData?.currentAssignment ?? null);

    if (!stats) return;
    document.getElementById('kpi-total').textContent = stats.nombreSoutiens;
    document.getElementById('kpi-duree').textContent =
        stats.dureeMoyenneMinutes > 0 ? stats.dureeMoyenneMinutes : '—';
    renderTable('tbody-academie', stats.parAcademie);
    renderTable('tbody-ips', stats.parTrancheIps);
}

window.addEventListener('load', init);
