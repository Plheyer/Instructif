import {getStudent} from "./localStorage-helper.js";

async function init() {
    console.log("Initialisation de la page");
    await loadRequestsList();
}

async function loadRequestsList() {
    try {
        const todo = 'requests-list';
        const student = getStudent();
        if (!student || !student.id) {
            console.error("Student not found to get his id");
            return;
        }
        const res = await fetch(`ActionServlet?todo=${todo}&studentId=${student.id}`)
            .then(r => r.json())
            .catch(() => null);
        const table = document.getElementById("table-body");
        table.innerHTML = "";
        console.log(res);

        for (const request of res) {
            const tdDate = document.createElement("td");
            tdDate.innerText = request.startDate;

            const tdSubject = document.createElement("td");
            tdSubject.innerText = `${request.subject} › ${request.topic}`;

            const tdBadge = document.createElement("td");
            const span = document.createElement("span");
            span.classList.add("badge", "badge-success");
            span.innerText = request.status;
            tdBadge.appendChild(span);

            const tdLink = document.createElement("td");
            const a = document.createElement("a");
            a.href = `detail-demande-bilan.html?id=${request.id}`;
            a.classList.add("link-action");
            a.innerText = "Voir le détail ›";
            tdLink.appendChild(a);

            const tr = document.createElement("tr");
            tr.appendChild(tdDate);
            tr.appendChild(tdSubject);
            tr.appendChild(tdBadge);
            tr.appendChild(tdLink);
            table.appendChild(tr);
        }
    } catch (err) {
        console.error('[detail-demande] erreur fetch interventions :', err);
    }
}

window.addEventListener("load", init);