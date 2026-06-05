import {formSubmit} from "./formSubmit.js";

async function initPage() {
    console.log("Initialisation de la page");
    document.getElementById("btnSubmit").addEventListener('click', function() {
        console.log("Click sur le bouton 'Inscrire'");
        inscription();
    });
}

async function inscription() {
    console.log("Appel de l'Action: Inscription");
    const todo = "register";
    const form = document.getElementById('form');
    const jsonResponse = await formSubmit(form, todo);
    if (jsonResponse && jsonResponse.result) {
        form.submit();
    }
    else {
        alert("Problème lors de l'inscription, veuillez réessayer plus tard.");
    }
}
window.onload = initPage;