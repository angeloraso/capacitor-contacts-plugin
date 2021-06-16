var capacitorContacts = (function (exports, core) {
    'use strict';

    const Contacts = core.registerPlugin('Contacts', {
        web: () => Promise.resolve().then(function () { return web; }).then(m => new m.ContactsWeb()),
    });

    /* eslint-disable import/order */
    class ContactsWeb extends core.WebPlugin {
        async getPermissions() {
            return { granted: false };
        }
        async requestPermissions() {
            return { granted: false };
        }
        async getContacts() {
            return { contacts: [] };
        }
        async addContact(data) {
            console.log('CONTACTS', data);
            return;
        }
        async deleteContact(data) {
            console.log('CONTACT ID', data);
            return;
        }
        async getGroups() {
            return { groups: [] };
        }
        async getContactGroups() {
            return { contactGroups: [] };
        }
    }

    var web = /*#__PURE__*/Object.freeze({
        __proto__: null,
        ContactsWeb: ContactsWeb
    });

    exports.Contacts = Contacts;

    Object.defineProperty(exports, '__esModule', { value: true });

    return exports;

}({}, capacitorExports));
//# sourceMappingURL=plugin.js.map
