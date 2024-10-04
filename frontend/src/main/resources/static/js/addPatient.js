document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('addPatientForm');

    // Gestion de la soumission du formulaire
    form.addEventListener('submit', function(event) {
        event.preventDefault();  // Empêche la soumission classique du formulaire

        const token = localStorage.getItem('token');  // Récupère le token JWT du localStorage
        if (!token) {
            alert('Vous devez être connecté pour ajouter un patient');
            return;
        }

        const formData = new FormData(form);  // Récupère les données du formulaire

        // Requête AJAX pour envoyer les données du formulaire et ajouter le patient
        fetch('/patients/add', {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token,  // Ajoute le JWT à l'en-tête Authorization
            },
            body: formData  // Envoie les données du formulaire
        })
            .then(response => {
                if (response.ok) {
                    window.location.href = '/patients';  // Redirige vers la liste des patients après succès
                } else if (response.status === 401) {
                    alert('Erreur : Non autorisé. Veuillez vous reconnecter.');
                } else {
                    throw new Error('Erreur lors de l\'ajout du patient');
                }
            })
            .catch(error => {
                console.error('Erreur :', error);
                alert('Erreur lors de l\'ajout du patient. Veuillez réessayer.');
            });
    });
});
