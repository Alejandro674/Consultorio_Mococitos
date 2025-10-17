import { createRouter, createWebHashHistory } from "vue-router";
import { securityStore } from "@/addons/store/general/security.ts";
// import { mixinApp } from "@/components/mixins/mixinapp";

let index = createRouter({
  history: createWebHashHistory(),
  routes: [
    { path: "/login", name: "Login", component: () => import("@/views/general/Login.vue"), meta: { guest: true },
      beforeEnter : () => { return securityStore().getJwt ? false : true }
    },
    {
      path: "/", name: "Index", component: () => import("@/views/general/Index.vue"),
      children: [
        { path: "/home", name: "Home", component: () => import("@/views/general/Home.vue") }
      ]
    }
  ]
});

index.beforeEach(async (to, _from, next) => {
    if (to.meta.guest) {
      next();
    } else {
      await securityStore().verifyJwt();
      next();
    }
});

export default index;