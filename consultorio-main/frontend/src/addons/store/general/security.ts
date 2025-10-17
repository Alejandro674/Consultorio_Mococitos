import { defineStore, getActivePinia } from "pinia";
import router from "@/addons/router";
import VueJwtDecode from "vue-jwt-decode";
import api from "@/plugins/api.ts";
import { auth, setHeader } from "@/plugins/setupInterceptor.ts";

export const securityStore = defineStore("security", {
    state: () => ({
        jwt: null as string | null, // ✅ ahora acepta string o null
        user: {} as any
    }),
    getters: {
        getJwt: (state) => state.jwt,
        getUser: (state) => state.user
    },
    actions: {
        verifyJwt() { // ✅ eliminamos 'next' no usado
            if (this.getJwt) {
                const decodedJwt: any = VueJwtDecode.decode(this.getJwt);
                const tiempo = decodedJwt.exp - Math.round(Date.now() / 1000);
                setHeader(decodedJwt.sub);

                if (tiempo < 0) {
                    this.limpiarStores();
                    // applicationStore().reportMessageError("La sesión ha expirado.");
                }
            } else {
                this.jwt = null;
                router.push({ name: "Login" });
            }
        },

        login(cred: { username: string; password: string }) {
            api
                .post("/login", {
                    nombreUsuario: cred.username,
                    password: cred.password
                })
                .then((response) => {
                    auth(response.headers.authorization);
                    this.decodificarToken(response.headers.authorization);
                    router.push("Home");
                })
                .finally(() => {});
        },

        logout() {
            api.post("/logout").then(() => this.limpiarStores());
        },

        decodificarToken(token: string) {
            this.jwt = token;
            const decoded: any = VueJwtDecode.decode(this.getJwt);
            this.user = JSON.stringify(decoded.usuario);
        },

        limpiarStores() {
            const pinia = getActivePinia();
            if (!pinia) return; // ✅ previene error “possibly undefined”
            const stores = Object.keys(pinia.state.value);
            stores.forEach((store) => {
                this.limpiarStore(store);
            });
            router.push({ name: "Login" });
            window.location.reload();
        },

        limpiarStore(store: string) {
            const pinia = getActivePinia() as any; // ✅ evitamos error de tipo
            const piniaStore = pinia?._s?.get(store);
            if (piniaStore && typeof piniaStore.$reset === "function") {
                piniaStore.$reset();
            }
        }
    },
    persist: true
});
