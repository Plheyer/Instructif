import { getStaff } from './localStorage-helper.js';
import { formatGrade } from './format.js';

async function fetchDetail(id) {
    try {
        const res = await fetch(`ActionServlet?todo=detail-demande&id=${id}`);
        return await res.json();
    } catch (err) {
        console.error('[detail-demande] erreur fetch :', err);
        return null;
    }
}

async function fetchInterventions() {
    try {
        const res = await fetch('ActionServlet?todo=my-interventions');
        return await res.json();
    } catch (err) {
        console.error('[detail-demande] erreur fetch interventions :', err);
        return null;
    }
}

function renderSidebar(staff, interventionsData, currentId) {
    if (staff) {
        document.getElementById('sidebar-initials').textContent =
            (staff.firstName[0] + staff.lastName[0]).toUpperCase();
        document.getElementById('sidebar-name').textContent =
            `${staff.firstName} ${staff.lastName}`;
    }

    const list = document.getElementById('recent-list');
    const recentes = interventionsData?.interventions?.slice(0, 5) ?? [];
    if (recentes.length === 0) {
        list.innerHTML = '<span style="font-size:0.75rem;color:var(--text-muted)">Aucune intervention</span>';
        return;
    }
    list.innerHTML = recentes.map(r => {
        const isActive = r.id === currentId ? 'style="background:#eef2ff"' : '';
        return `<a href="detail-demande-bilan.html?id=${r.id}" class="recent-item" ${isActive}>
            <span class="ri-theme">${r.topic}</span>
            <span class="ri-meta">${r.subject} · ${r.startDate}</span>
        </a>`;
    }).join('');
}

function statusBadge(status) {
    if (status === 'TERMINEE') return '<span class="badge-status badge-success">TERMINÉE</span>';
    if (status === 'ANNULEE')  return '<span class="badge-status badge-danger">ANNULÉE</span>';
    return '<span class="badge-status badge-warning">EN COURS</span>';
}

function render(data, currentId) {
    const { demande, bilan, intervenant } = data;

    // En-tête
    document.getElementById('page-title').textContent = demande.startDate
        ? `Demande du ${demande.startDate}`
        : 'Détail de la demande';
    document.getElementById('page-status').innerHTML = statusBadge(demande.status);

    // Demande
    document.getElementById('d-topic').textContent = demande.topic;
    document.getElementById('d-subject').textContent = demande.subject;
    document.getElementById('d-student').textContent =
        `${demande.studentFirstName} ${demande.studentLastName} (${formatGrade(demande.studentGrade)})`;
    document.getElementById('d-start').textContent = demande.startDate || '—';
    document.getElementById('d-end').textContent = demande.endDate || '—';
    document.getElementById('d-description').textContent = demande.description;

    const visioEl = document.getElementById('d-visio');
    if (demande.status !== 'EN_COURS' || !demande.meetingLink) {
        visioEl.textContent = 'Session terminée';
    } else {
        visioEl.innerHTML = `<a href="${demande.meetingLink}" target="_blank" class="link-action">Rejoindre la session ›</a>`;
    }

    // Bilan
    const bilanSection = document.getElementById('bilan-section');
    if (bilan) {
        document.getElementById('b-texte').textContent = bilan.texte;
        const conseilsBlock = document.getElementById('b-conseils-block');
        if (bilan.conseils) {
            document.getElementById('b-conseils').textContent = bilan.conseils;
        } else {
            conseilsBlock.style.display = 'none';
        }
        document.getElementById('i-initials').textContent = intervenant.initials;
        document.getElementById('i-name').textContent = intervenant.fullName;
        document.getElementById('i-type').textContent = intervenant.typeLabel;
    } else {
        bilanSection.style.display = 'none';
    }
}

async function init() {
    const params = new URLSearchParams(window.location.search);
    const id = parseInt(params.get('id'), 10);

    const staff = getStaff();
    const [detail, interventionsData] = await Promise.all([
        fetchDetail(id),
        fetchInterventions()
    ]);
    if (!detail) return;

    renderSidebar(staff, interventionsData, id);
    render(detail, id);
}

window.addEventListener('load', init);
