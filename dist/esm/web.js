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
//# sourceMappingURL=web.js.map