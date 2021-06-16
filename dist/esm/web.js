/* eslint-disable import/order */
import { WebPlugin } from '@capacitor/core';
export class ContactsWeb extends WebPlugin {
    async getPermissions() {
        return { granted: false };
    }
    async requestPermissions() {
        return { granted: false };
    }
    async getContacts() {
        return { contacts: [] };
    }
    async setContacts(contacts) {
        console.log('CONTACTS', contacts);
        return;
    }
    async deleteContact(contactId) {
        console.log('CONTACT ID', contactId);
        return;
    }
    async getGroups() {
        return { groups: [] };
    }
    async getContactGroups() {
        return { contactGroups: [] };
    }
}
//# sourceMappingURL=web.js.map