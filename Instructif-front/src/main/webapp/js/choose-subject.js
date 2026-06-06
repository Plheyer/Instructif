import { getStudent } from "./localStorage-helper.js";

async function init() {
    console.log("Initialisation de la page");
    const student = getStudent();
    if (!student || !student.firstName || !student.lastName || !student.schoolGrade) {
        console.error("Can't load the student")
        return;
    }
    await initials(student);
    await studentName(student);
    await schoolGrade(student);
}

async function initials(student) {
    document.getElementById("initials").innerText = student.firstName[0].toUpperCase() + student.lastName[0].toUpperCase()
}

async function studentName(student) {
    document.getElementById("student-name").innerText = student.firstName + ' ' + student.lastName;
}

async function schoolGrade(student) {
    document.getElementById("student-school-grade").innerText = student.schoolGrade + 'ème';
}

window.addEventListener("load", init);