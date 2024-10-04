document.addEventListener('DOMContentLoaded', function() {
    // Récupérer l'ID du patient depuis un attribut data ou via Thymeleaf
    const patientId = document.querySelector('input[name="id"]').value;  // Exemple d'accès via un champ caché dans le formulaire
    const token = localStorage.getItem('token');  // Assurez-vous que le JWT est stocké côté client

    // Fonction pour afficher les notes du patient
    function loadPatientNotes() {
        fetch(`http://localhost:8080/api/notes/${patientId}`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(notes => {
                const notesList = document.getElementById('notesList');
                notesList.innerHTML = '';  // Vider la liste actuelle

                notes.forEach(note => {
                    const li = document.createElement('li');
                    li.innerHTML = `<span>${note.noteDate}</span> - <span>${note.noteContent}</span>`;
                    notesList.appendChild(li);
                });
            })
            .catch(error => console.error('Erreur lors de la récupération des notes :', error));
    }

    // Fonction pour afficher le niveau de risque du patient
    function loadPatientRiskLevel() {
        fetch(`http://localhost:8080/api/risklevels/${patientId}`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(riskLevel => {
                const riskLevelDiv = document.getElementById('riskLevel');
                riskLevelDiv.textContent = `Niveau de risque : ${riskLevel}`;
            })
            .catch(error => console.error('Erreur lors de la récupération du niveau de risque :', error));
    }

    // Charger les notes et le niveau de risque lors du chargement de la page
    loadPatientNotes();
    loadPatientRiskLevel();
});
