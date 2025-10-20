<template>
  <div class="main-content">
    <div class="content">
      <div class="contenedor">
        <h1>¿Deseas generar una nueva contraseña?</h1>
        <p>
          Se generará una nueva contraseña para tu cuenta y será enviada a tu
          correo electrónico.
        </p>
        <p>Ten en cuenta lo siguiente:</p>
        <ul>
          <li>Esta contraseña reemplazará tu contraseña actual.</li>
          <li>
            Ya no podrás usar tu contraseña anterior para acceder.
          </li>
        </ul>
        <p>¿Deseas continuar?</p>

        <div class="form-actions">
          <button
              type="button"
              class="btn btn-primary"
              @click="generarNuevaPassword"
          >
            Sí, generar nueva contraseña
          </button>
          <button
              type="button"
              class="btn btn-secondary"
              @click="cancelar"
          >
            Cancelar
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';

const router = useRouter();

const generarNuevaPassword = () => {
  router.push({ name: 'NuevaPasswordGenerada' });
};

const cancelar = () => {
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

.contenedor p,
.contenedor ul {
  text-align: left;
  margin-bottom: 15px;
}

.contenedor ul {
  padding-left: 20px;
}

.contenedor ul li {
  margin-bottom: 8px;
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
  max-width: 300px;
}
</style>