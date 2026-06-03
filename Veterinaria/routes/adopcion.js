const express = require('express');
const router = express.Router();
const Mascota = require('../models/mascotasModel');
const Cita = require('../models/citaModel');
const authMiddleware = require('./authMiddleware');
const Sucursal = require('../models/sucursalesModel');
const Usuario = require('../models/userModel');

router.get("/", async (req,res)=>{
    try{
        const allMascotas = await Mascota.find();
        res.status(200).json(allMascotas);
    } catch(err){
        res.status(500).json({message:"Error en el servidor"});
    }
});

router.get("/:id", async (req,res)=>{
    const {id} = req.params;
    try{
        const mascota = await Mascota.findById(id);
        if(!mascota){
            return res.status(404).json({message:"Mascota no encontrada"});
        }
        res.status(200).json(mascota);
    }catch(err){
        res.status(500).json({message:"Error en el servidor"});
        }
});

router.post("/agendar/:mascotaId", authMiddleware, async (req, res) => {
    try {

        const mascota = await Mascota.findById(req.params.mascotaId);
        

        if (!mascota) {
            return res.status(404).json({ message: "Mascota no encontrada" });
        }
        const sucursal = await Sucursal.findOne();

        if(!sucursal){
            return res.status(404).json({ message: "Sucursal no encontrada" });
        }
        const usuario = await Usuario.findById(req.usuarioId);
        if(!usuario){
            return res.status(404).json({ message: "Usuario no encontrado" });
        }
        
        const ahora = new Date();

        
        const nuevaCita = new Cita({
            usuarioId: req.usuarioId,
            nombreMascota: mascota.nombre,
            nombrePersona: usuario.nombre,
            fecha: ahora,
            hora: ahora.getHours() + ":00",
            servicio: "Adopcion",
            sucursalId: sucursal._id
        });
        

        await nuevaCita.save();

        res.status(201).json({
            message: "Cita de adopción creada correctamente"
        });

    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

module.exports = router;