<template>
  <div class="main-content">
    <div class="content">
      <div class="contenedor">
        <h1>¡Contraseña generada con éxito!</h1>
        <p>
          Ya puedes acceder a tu cuenta con tu correo y la nueva contraseña enviada a tu email (revisa también spam).
        </p>
        <div class="form-actions">
          <button
              type="button"
              class="btn btn-primary"
              @click="irALogin"
          >
            Iniciar sesión
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';

const router = useRouter();

const irALogin = () => {
  router.push({ name: 'Login' });
};
</script>

<style scoped>
.contenedor {
  max-width: 500px;
  margin: 80px auto;
  padding: 30px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 10px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.contenedor h1 {
  margin-bottom: 20px;
  color: var(--primary-color);
}

.contenedor p {
  text-align: left;
  margin-bottom: 15px;
}

.form-actions {
  display: flex;
  flex-direction: column;
  gap: 10px;
  align-items: center;
  margin-top: 20px;
}

.form-actions .btn {
  width: 100%;
  max-width: 250px;
}
</style>