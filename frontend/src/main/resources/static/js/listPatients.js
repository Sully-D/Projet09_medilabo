function loadPatients() {
    const token = localStorage.getItem('token'); // Récupérer le token JWT du localStorage

    if (!token) {
        console.error('Pas de token disponible, redirection vers la page de login');
        window.location.href = '/login'; // Redirection vers la page de login si pas de token
        return;
    }

    fetch('http://localhost:8080/api/patients', {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + token,  // Ajouter le token JWT à l'en-tête
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else if (response.status === 401) {
                console.error('Token expiré ou invalide');
                window.location.href = '/login';  // Rediriger vers la page de login en cas d'échec
            } else {
                throw new Error('Erreur lors de la récupération des patients : ' + response.status);
            }
        })
        .then(patients => {
            // Afficher les patients dans le tableau
            displayPatients(patients);
        })
        .catch(error => console.error('Erreur :', error));
}

function displayPatients(patients) {
    const tbody = document.querySelector('tbody');
    tbody.innerHTML = '';  // Vider le tableau avant d'ajouter les nouvelles données

    patients.forEach(patient => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
      <td>${patient.firstName}</td>
      <td>${patient.lastName}</td>
      <td>${patient.dateOfBirth}</td>
      <td>${patient.gender}</td>
      <td>${patient.postalAddress}</td>
      <td>${patient.phone}</td>
      <td>
        <a href="/patients/edit/${patient.id}">Modifier</a> |
        <a href="/patients/delete/${patient.id}" onclick="return confirm('Voulez-vous vraiment supprimer ce patient ?')">Supprimer</a>
      </td>
    `;
        tbody.appendChild(tr);
    });
}

// Charger les patients lors du chargement de la page
document.addEventListener('DOMContentLoaded', loadPatients);
