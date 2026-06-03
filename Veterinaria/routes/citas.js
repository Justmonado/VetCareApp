const express = require('express');
const router = express.Router();
const citas = require('../models/citaModel');
const authMiddleware = require('./authMiddleware');

router.post("/", authMiddleware, async (req,res)=>{
    const {
        nombreMascota,
        nombrePersona,
        fecha,
        hora,
        servicio,
        sucursalId
    } = req.body;

    try{

        const newCita = new citas({
            usuarioId: req.usuarioId,
            nombreMascota,
            nombrePersona,
            fecha,
            hora,
            servicio,
            sucursalId
        });

        await newCita.save();

        res.status(201).json({
            message:"Cita registrada correctamente"
        });

    }catch(err){
        res.status(500).json({
            message: err.message
        });
    }
});

router.get("/miscitas", authMiddleware, async (req,res)=>{
    try{
        const allCitas = await citas.find({
            usuarioId: req.usuarioId
        });
        res.status(200).json(allCitas);
    }
    catch(err){
        res.status(500).json({message:"Error en el servidor"});
    }
});

router.delete("/:id", authMiddleware, async (req,res)=>{
    try{

        const cita = await citas.findOneAndDelete({
            _id: req.params.id,
            usuarioId: req.usuarioId
        });

        if(!cita){
            return res.status(404).json({
                message:"Cita no encontrada"
            });
        }

        res.status(200).json({
            message:"Cita eliminada correctamente"
        });

    }catch(err){
        res.status(500).json({
            message: err.message
        });
    }
});

module.exports = router;