import { getStaff } from './localStorage-helper.js';
import { formatGrade } from './format.js';

async function init() {
    const params = new URLSearchParams(window.location.search);
    const id = params.get('id');
    if (!id) {
        document.querySelector('main').innerHTML = '<p style="padding:2rem;color:red">ID de demande manquant dans l\'URL.</p>';
        return;
    }

    const detail = await fetch(`ActionServlet?todo=detail-demande&id=${id}`)
        .then(r => r.json())
        .catch(() => null);

    // Sidebar — profil
    const staff = getStaff();
    if (staff) {
        document.getElementById('sidebar-initials').textContent = (staff.firstName[0] + staff.lastName[0]).toUpperCase();
        document.getElementById('sidebar-name').textContent = `${staff.firstName} ${staff.lastName}`;
    }

    if (!detail) {
        document.querySelector('main').innerHTML = '<p style="padding:2rem;color:red">Impossible de charger la demande.</p>';
        return;
    }

    const { demande, eleve, etablissement } = detail;

    // Sidebar — affectation en cours (données déjà dans detail, pas besoin de my-interventions)
    document.getElementById('assignment-student').textContent =
        `${eleve.firstName} ${eleve.lastName} (${formatGrade(eleve.grade)})`;
    document.getElementById('assignment-topic').textContent =
        `${demande.subject} · ${demande.topic}`;
    document.getElementById('assignment-btn').href = `demande-prise-en-charge.html?id=${id}`;

    // Bannière
    document.getElementById('banner-text').textContent =
        `Tu as été sélectionné pour cette demande. Rejoins ${eleve.firstName} dès maintenant.`;

    // Visio
    if (demande.meetingLink) {
        document.getElementById('visio-link').href = demande.meetingLink;
        document.getElementById('visio-url').textContent = `Lien Visio : ${demande.meetingLink}`;
    }
    document.getElementById('end-btn').href = `redaction-bilan.html?id=${id}`;

    // Demande
    document.getElementById('dem-status').textContent = demande.status;
    document.getElementById('dem-start').textContent = demande.startDate;
    document.getElementById('dem-subject').textContent = demande.subject;
    document.getElementById('dem-topic').textContent = demande.topic;
    document.getElementById('dem-description').textContent = demande.description;

    // Elève
    document.getElementById('eleve-name').textContent = `${eleve.lastName} ${eleve.firstName}`;
    document.getElementById('eleve-birth').textContent = eleve.birthDate || '—';
    document.getElementById('eleve-grade').textContent = formatGrade(eleve.grade);
    document.getElementById('eleve-email').textContent = eleve.email;

    // Etablissement
    if (etablissement) {
        document.getElementById('etab-uai').textContent = etablissement.codeUAI;
        document.getElementById('etab-appellation').textContent = etablissement.appellation;
        document.getElementById('etab-secteur').textContent = etablissement.secteur;
        document.getElementById('etab-ips').textContent = etablissement.ips;
        document.getElementById('etab-adresse').textContent = etablissement.adresse;
        document.getElementById('etab-ville').textContent = `${etablissement.codePostal} ${etablissement.commune}`;
        document.getElementById('etab-academie').textContent =
            etablissement.libelleAcademie + (etablissement.libelleDepartement ? ` (${etablissement.libelleDepartement})` : '');
        document.getElementById('etab-coords').textContent =
            `${etablissement.latitude} ; ${etablissement.longitude}`;
    } else {
        document.getElementById('etab-card').innerHTML =
            '<p style="color:var(--text-muted);font-size:0.875rem;padding:0.5rem 0">Aucun établissement renseigné.</p>';
    }
}

window.addEventListener('load', init);
