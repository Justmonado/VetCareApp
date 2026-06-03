const express = require('express');
const router = express.Router();
const Servicios = require('../Models/servicioModel');


router.get("/sucursal/:sucursalId", async (req, res) => {
    try {
        const servicios = await Servicios.find({ sucursalId: req.params.sucursalId });
        res.json(servicios);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});


router.get("/:id", async (req,res)=>{
    try {
        const servicio = await Servicios.findById(req.params.id);
        if(!servicio){
            return res.status(404).json({message:"Servicio no encontrado"});
        } else {
            res.json(servicio);
        }
    } catch (err){
        res.status(500).json({message: err.message});
    }
});

router.get("/:id/horarios", async (req,res)=>{
    try {
        const servicio = await Servicios.findById(req.params.id);
        if(!servicio){
            return res.status(404).json({message:"Servicio no encontrado"});
        } else {
            res.json(servicio.horarios);
        }
    } catch (err){
        res.status(500).json({message: err.message});
    }
});

router.delete("/:id/horarios", async (req,res) =>{
    try {
        const { horarioId } = req.body;
        const servicio = await Servicios.findByIdAndUpdate(
            req.params.id,
            {$pull: { horarios: {_id: horarioId } } },
            {new: true}
        );
        if (!servicio) {
            return res.status(404).json({message: "Servicio no encontrado"});
        } else {
            res.json({message: "Horario eliminado"});
        }
    } catch (err){
        res.status(500).json({message: err.message});
    }
});

module.exports = router;