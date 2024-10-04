document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('searchPatientForm');

    form.addEventListener('submit', function(event) {
        event.preventDefault();  // Empêche la soumission classique du formulaire

        const token = localStorage.getItem('token');  // Récupère le token JWT du localStorage
        if (!token) {
            alert('Vous devez être connecté pour effectuer cette recherche');
            return;
        }

        const firstName = document.getElementById('firstName').value;
        const lastName = document.getElementById('lastName').value;

        // Requête AJAX pour rechercher un patient par prénom et nom
        fetch(`/patients/search?firstName=${encodeURIComponent(firstName)}&lastName=${encodeURIComponent(lastName)}`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,  // Ajoute le JWT à l'en-tête Authorization
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    return response.json();  // Convertir la réponse en JSON
                } else if (response.status === 401) {
                    alert('Erreur : Non autorisé. Veuillez vous reconnecter.');
                    throw new Error('Non autorisé');
                } else if (response.status === 404) {
                    alert('Aucun patient trouvé avec ces informations.');
                    throw new Error('Patient non trouvé');
                } else {
                    throw new Error('Erreur lors de la recherche du patient');
                }
            })
            .then(patient => {
                // Afficher les résultats de la recherche
                document.getElementById('searchResult').style.display = 'block';
                document.getElementById('patientName').textContent = `${patient.firstName} ${patient.lastName}`;
                document.getElementById('patientDob').textContent = new Date(patient.dateOfBirth).toLocaleDateString();
            })
            .catch(error => {
                console.error('Erreur :', error);
            });
    });
});
