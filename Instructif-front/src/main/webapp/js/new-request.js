import {getStudent} from "./localStorage-helper.js";

async function init() {
    console.log("Initialisation de la page");
    if (!checkIfSubjectInUri()) return;
    const subjectName = retrieveSubjectName();
    document.getElementById('subject-breadcrumb').innerText = subjectName;
    document.getElementById('selected-subject').innerText = subjectName;
    document.getElementById('theme-subject').innerText = `(matière : ${subjectName})`;
    await loadTheme();
    document.getElementById("btnSubmit").addEventListener("click", sendRequest);
}

const retrieveSubjectId = () => new URLSearchParams(document.location.search).get("subjectId");
const retrieveSubjectName = () => new URLSearchParams(document.location.search).get("subjectName");

function checkIfSubjectInUri() {
    let subjectId = retrieveSubjectId();
    let subjectName = retrieveSubjectName();
    if (!subjectId || !subjectName) {
        console.error("URI Parameters are missing, either themeId, subjectId or subjectName.");
        if (window.history.length > 1) history.back();
        else window.location.href = "index.html";
        return false;
    }
    return true;
}

async function loadTheme() {
    try {
        const params = new URLSearchParams({
            todo: 'get-themes',
            subjectId: retrieveSubjectId(),
            subjectName: retrieveSubjectName()
        });
        const res = await fetch(`ActionServlet?${params}`)
            .then(r => r.json())
            .catch(() => null);

        let firstItem = true;
        const select = document.getElementById("theme-select");
        for (const theme of res) {
            const input = document.createElement("input");
            input.type = "radio";
            input.name = "theme";
            input.required = true;

            const span = document.createElement("span");
            span.innerText = theme.intitule;

            const label = document.createElement("label");
            label.classList.add("theme-option");
            label.appendChild(input);
            label.appendChild(span);
            label.addEventListener("click", () => {
                document.querySelectorAll("#theme-select input[name='theme']").forEach(i => i.removeAttribute("checked"));
                document.querySelectorAll("#theme-select label").forEach(l => l.classList.remove("selected"));
                input.setAttribute("checked", "");
                label.classList.add("selected");
            });
            label.setAttribute("instructif-id", theme.id);
            if (firstItem) {
                input.setAttribute("checked", "");
                label.classList.add("selected");
                firstItem = false;
            }

            select.appendChild(label);
        }
    } catch (err) {
        console.error('[detail-demande] erreur fetch interventions :', err);
    }
}

async function sendRequest() {
    console.log("Appel de l'Action: Valider la demande");
    const form = document.getElementById("form");
    const themeId = document.querySelector("#form label.selected").getAttribute("instructif-id");
    const params = new URLSearchParams({
        todo: "send-request",
        studentId: getStudent().id,
        themeId,
        description: document.getElementById("desc").value
    });
    const url = `ActionServlet?${params.toString()}`;
    const jsonResponse = await fetch(url)
        .then(
            function(httpResponse) {
                return httpResponse.json();
            }
        )
        .catch(
            function(error) {
                console.log(error);
                return null;
            }
        );
    if (jsonResponse && jsonResponse.id) {
        form.action = `demande-en-cours-visio.html?requestId=${jsonResponse.id}`;
        form.submit();
    } else {
        alert("Problème lors de l'envoi de la demande, veuillez réessayer plus tard.");
    }
}

window.addEventListener("load", init);