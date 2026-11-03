import * as leaveApi from './leave-api-mock.js';
import { LeaveApiError } from './leave-api-error.js';

const texts = {
  fr: {
    language: 'Langue', balanceTitle: 'Solde de congés', employeeNumber: 'Numéro de salarié',
    show: 'Afficher', requestsTitle: 'Demandes de congés', filter: 'Filtrer par statut', all: 'Toutes',
    employee: 'Salarié', start: 'Début', end: 'Fin', days: 'Jours', status: 'Statut', actions: 'Actions',
    newRequest: 'Nouvelle demande', submit: 'Déposer', approve: 'Approuver', refuse: 'Refuser',
    balance: 'Solde : {0} jours', unexpected: 'Une erreur inattendue est survenue.',
    Pending: 'En attente', Approved: 'Approuvée', Refused: 'Refusée',
  },
  en: {
    language: 'Language', balanceTitle: 'Leave balance', employeeNumber: 'Employee number',
    show: 'Show', requestsTitle: 'Leave requests', filter: 'Filter by status', all: 'All',
    employee: 'Employee', start: 'Start', end: 'End', days: 'Days', status: 'Status', actions: 'Actions',
    newRequest: 'New request', submit: 'Submit', approve: 'Approve', refuse: 'Refuse',
    balance: 'Balance: {0} days', unexpected: 'An unexpected error occurred.',
    Pending: 'Pending', Approved: 'Approved', Refused: 'Refused',
  },
};

let language = 'fr';
let lastBalance = null;

const t = (key) => texts[language][key] ?? key;
const locale = () => (language === 'fr' ? 'fr-FR' : 'en-GB');
const formatNumber = (value) => new Intl.NumberFormat(locale(), { maximumFractionDigits: 2 }).format(value);
const formatDate = (iso) => new Intl.DateTimeFormat(locale()).format(new Date(iso));

const errorElement = document.querySelector('#error');

function showError(error) {
  errorElement.textContent = error instanceof LeaveApiError ? error.message : t('unexpected');
  if (!(error instanceof LeaveApiError)) {
    console.error(error);
  }
}

function clearError() {
  errorElement.textContent = '';
}

function translatePage() {
  document.documentElement.lang = language;
  for (const element of document.querySelectorAll('[data-i18n]')) {
    element.textContent = t(element.dataset.i18n);
  }
  renderBalance();
}

function renderBalance() {
  document.querySelector('#balance').textContent =
    lastBalance === null ? '' : t('balance').replace('{0}', formatNumber(lastBalance));
}

async function loadBalance() {
  clearError();
  try {
    const employeeId = Number(document.querySelector('#balance-employee').value);
    lastBalance = await leaveApi.getBalance(employeeId);
    renderBalance();
  } catch (error) {
    showError(error);
  }
}

function actionButton(label, action) {
  const button = document.createElement('button');
  button.type = 'button';
  button.textContent = label;
  button.addEventListener('click', async () => {
    clearError();
    try {
      await action();
      await loadRequests();
    } catch (error) {
      showError(error);
    }
  });
  return button;
}

async function loadRequests() {
  try {
    const status = document.querySelector('#status-filter').value || undefined;
    const requests = await leaveApi.getRequests(status);
    const body = document.querySelector('#requests');
    body.replaceChildren();
    for (const request of requests) {
      const row = document.createElement('tr');
      const cells = [
        request.id,
        request.employeeId,
        formatDate(request.startDate),
        formatDate(request.endDate),
        request.days,
        t(request.status),
      ];
      for (const value of cells) {
        const cell = document.createElement('td');
        cell.textContent = value ?? '';
        row.append(cell);
      }
      const actions = document.createElement('td');
      if (request.status === 'Pending') {
        actions.append(
          actionButton(t('approve'), () => leaveApi.approve(request.id)),
          actionButton(t('refuse'), () => leaveApi.refuse(request.id)),
        );
      }
      row.append(actions);
      body.append(row);
    }
  } catch (error) {
    showError(error);
  }
}

document.querySelector('#balance-form').addEventListener('submit', (event) => {
  event.preventDefault();
  loadBalance();
});

document.querySelector('#status-filter').addEventListener('change', loadRequests);

document.querySelector('#request-form').addEventListener('submit', async (event) => {
  event.preventDefault();
  clearError();
  const form = new FormData(event.target);
  try {
    await leaveApi.createRequest({
      employeeId: Number(form.get('employeeId')),
      startDate: form.get('startDate'),
      endDate: form.get('endDate'),
    });
    event.target.reset();
    await loadRequests();
  } catch (error) {
    showError(error);
  }
});

document.querySelector('#language').addEventListener('change', (event) => {
  language = event.target.value;
  translatePage();
  loadRequests();
});

translatePage();
loadBalance();
loadRequests();
