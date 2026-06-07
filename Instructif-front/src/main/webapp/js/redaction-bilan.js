import { getStaff } from './localStorage-helper.js';
import { formatGrade } from './format.js';

function parseFrDate(s) {
    // "dd/MM/yyyy HH:mm"
    const [datePart, timePart] = s.split(' ');
    const [day, month, year] = datePart.split('/');
    const [hours, minutes] = timePart.split(':');
    return new Date(year, month - 1, day, hours, minutes);
}

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

    const { demande, eleve } = detail;

    // Sidebar — affectation en cours
    document.getElementById('assignment-student').textContent =
        `${eleve.firstName} ${eleve.lastName} (${formatGrade(eleve.grade)})`;
    document.getElementById('assignment-topic').textContent =
        `${demande.subject} · ${demande.topic}`;
    document.getElementById('assignment-btn').href = `demande-prise-en-charge.html?id=${id}`;

    // En-tête
    document.getElementById('header-date').textContent = `Demande du ${demande.startDate}`;

    // Résumé
    document.getElementById('sum-eleve').textContent = `${eleve.firstName} ${eleve.lastName} (${formatGrade(eleve.grade)})`;
    document.getElementById('sum-theme').textContent = `${demande.topic} (${demande.subject})`;

    const duree = demande.startDate
        ? Math.round((Date.now() - parseFrDate(demande.startDate)) / 60000)
        : null;
    document.getElementById('sum-duree').textContent = duree != null ? `${duree} min` : '—';

    document.getElementById('description-text').textContent = demande.description;

    // Soumission du formulaire
    document.getElementById('bilan-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        const texte    = document.getElementById('bilan').value.trim();
        const conseils = document.getElementById('conseils').value.trim();
        const btn      = document.getElementById('submit-btn');

        if (!texte) {
            document.getElementById('form-error').textContent = 'Le texte du bilan est obligatoire.';
            return;
        }
        btn.disabled = true;
        btn.textContent = 'Envoi…';

        const body = new URLSearchParams({ id, texte, conseils });
        const res = await fetch('ActionServlet?todo=envoyer-bilan', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body
        }).then(r => r.json()).catch(() => null);

        if (res?.success) {
            window.location.href = 'mes-interventions.html';
        } else {
            document.getElementById('form-error').textContent = 'Une erreur est survenue. Vérifie que la demande est bien EN_COURS.';
            btn.disabled = false;
            btn.textContent = 'Envoyer le bilan';
        }
    });
}

window.addEventListener('load', init);
