'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var core = require('@capacitor/core');

const Contacts = core.registerPlugin('Contacts', {
    web: () => Promise.resolve().then(function () { return web; }).then(m => new m.ContactsWeb()),
});

/* eslint-disable @typescript-eslint/no-unused-vars */
class ContactsWeb extends core.WebPlugin {
    async checkPermissions() {
        throw this.unimplemented('Not implemented on web.');
    }
    async requestPermissions() {
        throw this.unimplemented('Not implemented on web.');
    }
    async getContacts() {
        throw this.unimplemented('Not implemented on web.');
    }
    async createContact(_data) {
        throw this.unimplemented('Not implemented on web.');
    }
    async addToExistingContact(_data) {
        throw this.unimplemented('Not implemented on web.');
    }
    async deleteContact(_data) {
        throw this.unimplemented('Not implemented on web.');
    }
    async getGroups() {
        throw this.unimplemented('Not implemented on web.');
    }
    async getContactGroups() {
        throw this.unimplemented('Not implemented on web.');
    }
}

var web = /*#__PURE__*/Object.freeze({
    __proto__: null,
    ContactsWeb: ContactsWeb
});

exports.Contacts = Contacts;
//# sourceMappingURL=plugin.cjs.js.map
