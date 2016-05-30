(function() {
  'use strict';

  var module = angular.module('services');

  // Export translations as a JSON Object
  module.constant('translations_DE', {
    boards: {
      boards: "Übersicht Boards",
      createBoard: "Board erstellen",
      updateBoard: "Board bearbeiten",
      table: {
        title: "Bezeichnung",
        cntMembers: "Anzahl Mitglieder",
        owner: "Besitzer",
        actions: "Bearbeiten/Löschen",
        actionNotOwner: "Nicht der Besitzer des Boards"
      },
      form: {
        title: "Bezeichnung",
        requiredTitle: "Bitte geben Sie einen Board Titel an",
        members: "Mitglieder",
        addMember: "Mitglied hinzufügen"
      }
    },
    tasks: {
      createTitle: "Task erstellen",
      updateTitle: "Task updaten",
      form: {
          boardSelection: "Board",
          description: "Bezeichnung",
          requiredDescription: "Bitte geben Sie einen Task Beschreibung an",
          person: "Zuständige Person",
          attachment: "Anhang",
          uploadSize: "Folgende {{counter}} Dateien werden angehängt",
          addAttachment: "Hinzufügen",
          dueDate: "Zu erledigen bis"
      },
      dueDate: "Erledigen bis",
      doneDate: "Erledigt am",
      doTitle: "Todo",
      doingTitle: "In Bearbeitung",
      doneTitle: "Erledigt",
      filter: "Tasks filtern"
    },
    app: {
      title: "ToDooooo",
      navigation: "Navigation",
      footer: "ToDooooo macht deine Todos do and DONE"
    },
    main: {
      toBoards: "Zu deinen Boards!",
      registerNow: "Jetzt anmelden",
      lead: "Bleib immer organisiert!",
      aProjectFrom: "Ein Projekt von Tobias Giess und Denis Augsburger für das Modul WODSS der FHNW"
    },
    modal: {
      sureToDelete: "Sind sie sicher, dass sie {{name}} löschen möchten?"
    },
    nav: {
      home: "Start",
      register: 'Jetzt Registrieren',
      passwordReset: 'Passwort vergessen?',
      boards: 'Boards',
      user: 'Profil bearbeiten',
      login: 'Login',
      logout: "Logout",
      tasks: 'Alle Tasks anzeigen',
      about: 'Über',
      loginBack: "Zurück zum Login"
    },
    btn: {
      create: "Erstellen",
      update: "Bearbeiten",
      register: "Registrieren",
      close: "Schliessen",
      today: "Heute",
      clear: "Leeren",
      yes: "Ja",
      no: "Nein"
    },
    login: {
      loginBtn: "Login",
      emailRequired: "Bitte geben Sie Ihre Email Adresse ein",
      emailWrong: "Keine gültige Email Adresse",
      passwordRequired: "Bitte geben Sie ihr Passwort an",
      failed: "Passwort oder EMail Adresse falsch. Bitte überprüfen Sie ihre Eingabe."
    },
    user: {
        email: "Email Adresse",
        name: "Benutzername",
        password: "Passwort",
        password2: "Passwort wiederholen",
        editProfile: "Profil bearbeiten",
        saved: "Profil wurde aktualisiert",
        error: "Profil konnte aufgrund eines Fehlers nicht aktualisiert werden"
    },

    register: {
      validationRequired: "Bitte validieren Sie ihre Email Adresse mit dem Aktivierungslink, den wir ihnen gesendet haben.",
      alreadyRegistered: "Diese Email wurde bereits registriert. Bitte loggen Sie sich ein oder setzen Sie ihr Passwort zurück",
      errorHappened: "Die Registrierung ist aus unbekannten Gründen fehlgeschlagen",
      emailRequired: "Bitte geben Sie Ihre Email Adresse ein",
      passwordTooShort: "Bitte geben Sie ein Passwort mit mindestens 6 Zeichen an",
      emailWrong: "Keine gültige Email Adresse",
      passwordRequired: "Bitte geben Sie ein Passwort an",
      passwordMustMatch: "Die Passwörter stimmen nicht überein",
      repeatPassword: "Bitte wiederholen Sie ihr eingegebenes Passwort"
    },
    reset: {
      resetBtn: "Account zurücksetzen",
      emailSent: "Bitte prüfen Sie ihre EMail Postfach, um ihr Passwort zurückzusetzen",
      notSent: "Ihr Account konnte nicht zurückgesetzt werden. Sind sie registriert?",
      error: {
        title: "Ihr Resetcode ist ungültig",
        text: "Bitte kontrollieren Sie den Link im erhaltenen Email und rufen Ihn erneut auf"
      },
      gone: {
        title: "Resetcode abgelaufen",
        text: "Bitte fordern Sie einen neuen Resetcode an"
      }
    },

    validation:{
        successful: "Ihre EMail Adresse wurde erfolgreich validiert",
        error: {
          title: "Ihre Validierung ist ungültig",
          text: "Bitte kontrollieren Sie den Link im erhaltenen Email und rufen Ihn erneut auf"
        },
        empty: {
          title: "Kein Validierungscode angegeben",
          text: "Bitte kontrollieren Sie den Link im erhaltenen Email und rufen Ihn erneut auf"
        }
    },
    error: {
      errorPage: "Fehlerseite"
    },
    filter:{
      searchText: "Suche",
      assignee: "Zuständige Person",
      selectAssignee: "Alle"
    }
  });

})();
