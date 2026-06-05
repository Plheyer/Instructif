async function initPage() {
    console.log("Initialisation de la page");
    document.getElementById("btnSubmit").addEventListener('click', function() {
        console.log("Click sur le bouton 'Inscrire'");
        inscription();
    });
}

async function inscription() {
    console.log("Appel de l'Action: Inscription");
    const todo = "inscription";
    const form = document.getElementById('form');
    const formData = new FormData(form);
    const params = new URLSearchParams();
    formData.forEach((value, key) => {
        params.append(key, value.toString());
    });
    const url = `ActionServlet?todo=${todo}&${params.toString()}`;
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
    if (jsonResponse && jsonResponse.result) {
        form.submit();
    }
    else {
        alert("Problème lors de l'inscription, veuillez réessayer plus tard.");
    }
}
window.onload = initPage;