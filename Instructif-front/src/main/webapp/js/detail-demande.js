import {getStaff, getStudent} from './localStorage-helper.js';
import { formatGrade } from './format.js';
import {loadRecentList} from "./aside-helper.js";
import {getMeStaff} from "./auth-middleware-helper.js";

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

async function renderSidebar(user, interventionsData, currentId, isStaff) {
    if (user) {
        document.getElementById('initials').textContent =
            (user.firstName[0] + user.lastName[0]).toUpperCase();
        document.getElementById('name').textContent =
            `${user.firstName} ${user.lastName}`;
    }

    const nav = document.getElementById("nav");
    nav.innerText = "";
    const list = document.getElementById('recent-list');

    if (isStaff) {
        const div = document.createElement("div");
        div.classList.add("nav-label");
        div.innerText = "Intervenant";
        const aInterventions = document.createElement("a");
        aInterventions.classList.add("nav-item");
        aInterventions.innerText = "Mes interventions";
        aInterventions.href = "mes-interventions.html";
        const aStats = document.createElement("a");
        aStats.classList.add("nav-item");
        aStats.innerText = "Statistiques";
        aStats.href = "statistiques.html";
        nav.appendChild(div);
        nav.appendChild(aInterventions);
        nav.appendChild(aStats);

        document.getElementById("recent-list-title").innerText = "Interventions récentes";

        document.getElementById("backToList").href = "mes-interventions.html";

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
    } else {
        const aSubject = document.createElement("a");
        aSubject.classList.add("nav-item");
        aSubject.href = "choix-matiere.html";
        aSubject.innerText = "Matières";
        const aRequest = document.createElement("a");
        aRequest.classList.add("nav-item", "active");
        aRequest.innerText = "Ma demande";
        aRequest.href = "#";
        const aRequests = document.createElement("a");
        aRequests.classList.add("nav-item");
        aRequests.innerText = "Mes demandes";
        aRequests.href = "mes-demandes.html";
        nav.appendChild(aSubject);
        nav.appendChild(aRequest);
        nav.appendChild(aRequests);

        document.getElementById("recent-list-title").innerText = "Demandes récentes";

        const schoolGradeDiv = document.createElement("div");
        schoolGradeDiv.id = "student-school-grade";
        schoolGradeDiv.style = "font-size: 0.75rem; color: var(--text-muted);";
        schoolGradeDiv.innerText = user.schoolGrade + 'ème';
        document.getElementById("footer-text").appendChild(schoolGradeDiv);

        document.getElementById("backToList").href = "mes-demandes.html";

        await loadRecentList();
    }
}

function statusBadge(status) {
    if (status === 'TERMINEE') return '<span class="badge-status badge-success">TERMINÉE</span>';
    if (status === 'ANNULEE')  return '<span class="badge-status badge-danger">ANNULÉE</span>';
    return '<span class="badge-status badge-warning">EN COURS</span>';
}

function render(data, currentId) {
    const { demande, eleve, bilan, intervenant } = data;

    // En-tête
    document.getElementById('page-title').textContent = demande.startDate
        ? `Demande du ${demande.startDate}`
        : 'Détail de la demande';
    document.getElementById('page-status').innerHTML = statusBadge(demande.status);

    // Demande
    document.getElementById('d-topic').textContent = demande.topic;
    document.getElementById('d-subject').textContent = demande.subject;
    document.getElementById('d-student').textContent =
        `${eleve.firstName} ${eleve.lastName} (${formatGrade(eleve.grade)})`;
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
    const student = getStudent();
    const meStaff = await getMeStaff()
    const [detail, interventionsData] = await Promise.all([
        fetchDetail(id),
        fetchInterventions()
    ]);
    if (!detail) return;

    const user = staff === null && meStaff === null ? student : staff;
    const isStaff = staff !== null;
    await renderSidebar(user, interventionsData, id, isStaff);
    render(detail, id);
}

window.addEventListener('load', init);
