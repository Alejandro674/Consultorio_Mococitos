<template>
  <div class="card flex justify-center">
    <div class="flex flex-col gap-1">
      <InputText name="username" type="text" placeholder="Username" v-model="cred.username" />
    </div>
    <div class="flex flex-col gap-1">
      <InputText name="password" type="password" placeholder="Password" v-model="cred.password" />
    </div>
    <Button type="submit" severity="success" label="Iniciar Sesion" @click="security.login( cred )" />
  </div>
</template>

<script lang="ts" setup>
import { securityStore } from "@/addons/store/general/security.ts";
import {ref} from "vue";

const security = securityStore();

const cred = ref({
  username: '',
  password: ''
});

</script>