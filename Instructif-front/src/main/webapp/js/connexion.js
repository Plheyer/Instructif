import {formSubmit} from "./formSubmit.js";
import {setStaff, setStudent} from "./localStorage-helper.js";

async function init() {
    console.log("Initialisation de la page");
    document.getElementById("btnSubmitStudent").addEventListener('click', function() {
        console.log("Click sur le bouton 'Connexion Eleve'");
        connexionStudent();
    });
    document.getElementById("btnSubmitStaff").addEventListener('click', function() {
        console.log("Click sur le bouton 'Connexion Eleve'");
        connexionStaff();
    });
}

async function connexionStudent() {
    console.log("Appel de l'Action: Connexion Eleve");
    const todo = "login-student";
    const form = document.getElementById('student-form');
    const jsonResponse = await formSubmit(form, todo);
    if (jsonResponse && jsonResponse.id) {
        setStudent(jsonResponse);
        window.location.href = form.action;
    }
    else {
        alert("Problème lors de la connexion, veuillez réessayer plus tard.");
    }
}

async function connexionStaff() {
    console.log("Appel de l'Action: Connexion Staff");
    const todo = "login-staff";
    const form = document.getElementById('staff-form');
    const jsonResponse = await formSubmit(form, todo);
    if (jsonResponse && jsonResponse.id) {
        setStaff(jsonResponse);
        window.location.href = form.action;
    }
    else {
        alert("Problème lors de la connexion, veuillez réessayer plus tard.");
    }
}

window.addEventListener("load", init);