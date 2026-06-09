import { getStudent } from "./localStorage-helper.js";

async function init() {
    const student = getStudent();
    if (!student || !student.firstName || !student.lastName || !student.schoolGrade) {
        console.error("Can't load the student")
        return;
    }
    initials(student);
    studentName(student);
    schoolGrade(student);
    await loadRecentList();
}

function initials(student) {
    document.getElementById("initials").innerText = student.firstName[0].toUpperCase() + student.lastName[0].toUpperCase()
}

function studentName(student) {
    document.getElementById("student-name").innerText = student.firstName + ' ' + student.lastName;
}

function schoolGrade(student) {
    document.getElementById("student-school-grade").innerText = student.schoolGrade + 'ème';
}

async function loadRecentList() {
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
}

window.addEventListener("load", init);