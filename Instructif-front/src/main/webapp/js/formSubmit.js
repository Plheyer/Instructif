export async function formSubmit(form, todo) {
    const formData = new FormData(form);
    const params = new URLSearchParams();
    formData.forEach((value, key) => {
        params.append(key, value.toString());
    });
    const url = `ActionServlet?todo=${todo}&${params.toString()}`;
    return await fetch(url)
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
}
