/*
 * Script de datos de prueba.
 * Llena la base con sucursales, servicios (con horarios) y mascotas
 * que coinciden con los mockups. Ejecuta:  node seed.js
 */
const mongoose = require('mongoose');
require('dotenv').config();

const Sucursal = require('./Models/sucursalesModel');
const Servicio = require('./Models/servicioModel');
const Mascota  = require('./Models/mascotasModel');

function generarHorarios() {
    const horas = ["10:00", "11:00", "12:00", "16:00", "17:00", "18:00"];
    const horarios = [];
    const hoy = new Date();
    for (let d = 1; d <= 5; d++) {
        const fecha = new Date(hoy);
        fecha.setDate(hoy.getDate() + d);
        fecha.setHours(0, 0, 0, 0);
        for (const hora of horas) {
            horarios.push({ fecha, hora });
        }
    }
    return horarios;
}

async function seed() {
    await mongoose.connect(process.env.MONGO_URI);
    console.log("Conectado a Mongo");


    const sucursales = await Sucursal.insertMany([
        {
            nombre: "VetCare Panorama",
            direccion: "P.º de los Insurgentes 1800-Interior 3, Panorama, 37160 León de los Aldama, Gto.",
            horario: "Lun–Sáb · 9:00–20:00",
            photoUrl: "https://universidadeuropea.com/resources/media/images/medicina-veterinaria-800x450.width-640.jpg",
            servicios: ["Consulta Medica", "Estetica", "Hotel", "Adopcion"],
        },
        {
            nombre: "VetCare Plaza Mayor",
            direccion: "Boulevard Juan José, Blvd. Juan Alonso de Torres Pte. 2077, Valle del Campestre, 37150 León de los Aldama, Gto.",
            horario: "Lun–Dom · 8:00–22:00",
            photoUrl: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ2tV2qG1pQKVcacEpt-NWDWt30kW6SJ6u6Sg&s",
            servicios: ["Consulta Medica", "Estetica"],
        },
    ]);

    const serviciosDocs = [];
    for (const suc of sucursales) {
        for (const nombre of suc.servicios) {
            serviciosDocs.push({
                sucursalId: suc._id,
                nombre,
                horarios: generarHorarios(),
                disponible: true,
            });
        }
    }
    await Servicio.insertMany(serviciosDocs);

    await Mascota.insertMany([
        {
            nombre: "Escosito", especie: "Gato", raza: "Fold escocés", sexo: "Hembra", edad: 1,
            descripcion: "Gato juguetón y muy sociable, ideal para familias con niños.",
            photoUrl: "https://www.zooplus.es/magazine/wp-content/uploads/2019/02/Scottish-fold-3.webp",
        },
        {
            nombre: "Rocky", especie: "Perro", raza: "Beagle", sexo: "Macho", edad: 1,
            descripcion: "Perro noble y protector, busca un hogar tranquilo.",
            photoUrl: "https://cdn0.uncomo.com/es/posts/9/3/9/como_saber_si_un_beagle_es_de_raza_50939_600.jpg",
        },{
            nombre: "Odin", especie: "Gato", raza: "Maine Coon", sexo: "Macho", edad: 1,
            descripcion: "Gato noble y juguetón, busca un hogar tranquilo.",
            photoUrl: "https://rhrpets.com/cdn/shop/articles/1._Odin_front_39823561-9ec9-48a0-ac98-56df485b1c8e.png?v=1758200883",
        },
    ]);

    console.log("Datos de prueba insertados correctamente.");
    await mongoose.disconnect();
    process.exit(0);
}

seed().catch((err) => {
    console.error(err);
    process.exit(1);
});
