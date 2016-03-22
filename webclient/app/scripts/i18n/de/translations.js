(function() {
  'use strict';

  var module = angular.module('services');

  // Export translations as a JSON Object
  module.constant('translations_DE', {
    boards: {
      boards: "Übersicht Boards",
      createBoard: "Board erstellen",
      form: {
        title: "Bezeichnung",
        requiredTitle: "Bitte geben Sie einen Board Titel an"
      }
    },
    tasks: {
      createTitle: "Task erstellen",
      updateTitle: "Task updaten",
      form: {
          title: "Bezeichnung",
          requiredTitle: "Bitte geben Sie einen Task Titel an",
          person: "Person",
          attachment: "Anhang"
      }
    },
    app: {
      title: "ToDooooo",
      navigation: "Navigation",
      footer: "ToDooooo macht deine Todos do and DONE"
    },
    main: {
      toBoards: "Zu deinen Boards!",
      lead: "Bleib immer organisiert!"
    },
    nav: {
      home: "Start",
      register: 'Registrieren',
      boards: 'Boards',
      login: 'Login',
      tasks: 'Tasks',
      about: 'Über',
      loginBack: "Zurück zum Login",
    },
    btn: {
      create: "Erstellen",
      register: "Registrieren",

    },
    login: {
      emailRequired: "Bitte geben Sie Ihre Email Adresse ein",
      emailWrong: "Keine gültige Email Adresse"
    },
    user: {
        email: "Email Adresse",
        password: "Passwort",
        password2: "Passwort wiederholen"
    },

    register: {
      emailRequired: "Bitte geben Sie Ihre Email Adresse ein",
      emailWrong: "Keine gültige Email Adresse",
      passwordRequired: "Bitte geben Sie ein Passwort an",
      repeatPassword: "Bitte wiederholen Sie ihr eingegebenes Passwort"
    },
    error: {
      errorPage: "Fehlerseite"
    }

  });

})();
