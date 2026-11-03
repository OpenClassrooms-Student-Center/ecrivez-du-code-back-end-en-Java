// Erreur interceptée par app.js pour afficher un message à l'utilisateur.
export class LeaveApiError extends Error {
  constructor(message) {
    super(message);
    this.name = 'LeaveApiError';
  }
}
