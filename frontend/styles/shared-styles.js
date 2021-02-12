// eagerly import theme styles so as we can override them
import '@vaadin/vaadin-material-styles/all-imports';

const $_documentContainer = document.createElement('template');

$_documentContainer.innerHTML = `
<custom-style>
  <style>
    html {
    }

    vaadin-devmode-gizmo {
        visibility: hidden;
      }
  </style>
</custom-style>


<custom-style>
  <style>
    html {
      overflow: hidden;
      font: var(--material-font-family);
    }

    vaadin-devmode-gizmo {
        visibility: hidden;
      }
  </style>
</custom-style>

<dom-module id="app-layout" theme-for="vaadin-app-layout">
  <template>
    <style>
      :host(:not([dir='rtl']):not([overlay])) [part='drawer'] {
        border-right: none;
        box-shadow: var(--material-secondary-background-color);
        background-color: var(--material-background-color);
        z-index: 1;
      }
      :host([dir='rtl']:not([overlay])) [part='drawer'] { //phone mode
        border-left: none;
        box-shadow: var(--material-secondary-background-color);
        background-color: var(--material-background-color);
        z-index: 1;
      }
      [part='navbar'] {
        box-shadow: var(--material-secondary-background-color);
      }
      :host([overlay]) [part="backdrop"] {
        z-index: 0;
      }
    </style>
  </template>
</dom-module>`;

document.head.appendChild($_documentContainer.content);
