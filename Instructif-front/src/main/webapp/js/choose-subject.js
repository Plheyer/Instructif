async function init() {
    console.log("Initialisation de la page");
    await loadSubjectList();
    document.getElementById('search-bar').addEventListener('input', loadSubjectList);
    document.getElementById('theme-checkbox').addEventListener('change', loadSubjectList);
    document.getElementById('subject-checkbox').addEventListener('change', loadSubjectList);
    document.getElementById('subject-select').addEventListener('change', loadSubjectList);
}

async function loadSubjectList() {
    try {
        const todo = 'subject-list';
        const res = await fetch(`ActionServlet?todo=${todo}`)
            .then(r => r.json())
            .catch(() => null);
        const list = document.getElementById("subject-grid");
        list.innerHTML = "";

        const filteredSubjects = new Set();
        const filteredThemes = new Set();
        const searchedValue = document.getElementById("search-bar").value;
        if (searchedValue.length) {
            if (document.getElementById('theme-checkbox').checked) {
                for (const subject of res) {
                    for (const theme of subject.themes) {
                        if (theme.intitule.toLowerCase().includes(searchedValue.toLowerCase())) {
                            filteredThemes.add(theme);
                            filteredSubjects.add(subject);
                        }
                    }
                }
            }
            if (document.getElementById('subject-checkbox').checked) {
                for (const subject of res) {
                    if (subject.name.toLowerCase().includes(searchedValue.toLowerCase())) {
                        filteredSubjects.add(subject);
                    }
                }
            }
        } else {
            for (const r of res) filteredSubjects.add(r);
        }

        for (const subject of filteredSubjects) {
            const icon = document.createElement("div");
            icon.classList.add("subject-icon");
            icon.style = 'background: #fff7ed; color: #f97316;';
            icon.textContent = '🌍';

            const infoTitle = document.createElement("h3");
            infoTitle.textContent = subject.name;
            const infoDesc = document.createElement("p");
            infoDesc.textContent = truncate(subject.themes.map(t => t.intitule).join(', '), 65);
            const info = document.createElement("div");
            info.classList.add("subject-info");
            info.appendChild(infoTitle);
            info.appendChild(infoDesc);

            const footer = document.createElement("div");
            footer.classList.add("subject-footer");
            const footerThemesNumber = document.createElement("span");
            footerThemesNumber.textContent = `${subject.themes.length} thème(s)`;
            const footerChooseSubject = document.createElement("span");
            footerChooseSubject.classList.add("btn-choose");
            footerChooseSubject.textContent = "Choisir un thème ›";
            footer.appendChild(footerThemesNumber);
            footer.appendChild(footerChooseSubject);

            const a = document.createElement("a");
            a.classList.add("subject-card");
            a.href = `nouvelle-demande.html?subjectId=${subject.id}&subjectName=${subject.name}`;
            a.appendChild(icon);
            a.appendChild(info);
            a.appendChild(footer);
            list.appendChild(a);
        }

        if (document.getElementById('subject-select').value === 'Trier par : Z → A') {
            list.append(...Array.from(list.childNodes).reverse());
        }
        document.getElementById("subjects-results-number").innerText = `${filteredSubjects.size} matière(s) · ${filteredThemes.size} thème(s) correspondant(s)`;
    } catch (err) {
        console.error('[detail-demande] erreur fetch interventions :', err);
    }
}

function truncate(str, maxLength) {
    return str.length > maxLength
        ? str.slice(0, maxLength) + "..."
        : str;
}

window.addEventListener("load", init);