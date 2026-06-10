import {getStudent} from "./localStorage-helper.js";

export async function loadRecentList() {
    const todo = 'recent-list';
    const student = getStudent();
    if (!student || !student.id) {
        console.error("Student not found to get his id");
        return;
    }
    const res = await fetch(`ActionServlet?todo=${todo}&studentId=${student.id}`)
        .then(r => r.json())
        .catch(() => []);
    const list = document.getElementById("recent-list");
    list.innerHTML = "";
    for (const recent of res) {
        const a = document.createElement("a");
        a.href = `detail-demande-bilan.html?id=${recent.id}`
        a.classList.add("recent-item");

        const spanTheme = document.createElement("span");
        spanTheme.classList.add("ri-theme");
        spanTheme.innerText = recent.topic;

        const spanMeta = document.createElement("span");
        spanMeta.classList.add("ri-meta");
        spanMeta.innerText = `${recent.subject} · ${recent.startDate}`;

        a.appendChild(spanTheme);
        a.appendChild(spanMeta);

        list.appendChild(a);
    }
    if (res.length === 0) {
        list.innerHTML = '<span style="font-size:0.75rem;color:var(--text-muted)">Aucune intervention</span>';
    }
}