async function init() {
    console.log("Initialisation de la page");
    if (!checkIfRequestIdInUri()) return;
    await loadRequest();
}

const retrieveRequestId = () => new URLSearchParams(document.location.search).get("requestId");

function checkIfRequestIdInUri() {
    const requestId = retrieveRequestId();
    if (!requestId) {
        console.error("URI Parameters are missing: request.");
        if (window.history.length > 1) history.back();
        else window.location.href = "index.html";
        return false;
    }
    return true;
}

async function loadRequest() {
    try {
        const todo = 'request';
        const requestId = retrieveRequestId();
        const res = await fetch(`ActionServlet?todo=${todo}&requestId=${requestId}`)
            .then(r => r.json())
            .catch(() => null);
        const status = document.getElementById("status");
        status.innerText = res.status === "EN_COURS" ? "Session en cours" : (res.status === "TERMINEE" ? "Session terminée" : "Session annulée");
        if (res.status !== "EN_COURS") {
            document.getElementById("link-card").style.display = 'none';
        }

        const subjectTheme = document.getElementById("subject-theme");
        subjectTheme.innerText = `${res.theme} · ${res.subject}`;

        const description = document.getElementById("description");
        description.innerText = res.description;

        const btnVisio = document.getElementById("btn-visio");
        btnVisio.innerText = res.visioLink;

        const startDate = document.getElementById("startDate");
        startDate.innerText = res.startDate !== "" && res.startDate !== "null" ? res.startDate : "N/A";

        const endDate = document.getElementById("endDate");
        endDate.innerText = res.endDate !== "" && res.endDate !== "null" ? res.endDate : "(Disponible après la séance)";
    } catch (err) {
        console.error('[detail-demande] erreur fetch interventions :', err);
    }
}

window.addEventListener("load", init);