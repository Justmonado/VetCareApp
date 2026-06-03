const express = require('express');
const router = express.Router();
const Mascotas = require('../Models/mascotasModel');

router.get("/", async (req,res)=>{
    try{
        const mascotas = await Mascotas.find();
        res.json(mascotas);
    } catch (err) {
        res.status(500).json({message:err.message});
    }
});

router.get("/:id", async(req,res)=>{
    try {
        const mascota = await Mascotas.findById(req.params.id);
        if (!mascota){
            return res.status(404).json({message: "Mascota no encontrada"});
        } else {
            res.json(mascota);
        }
    } catch (err) {
        res.status(500).json({message:err.message});
    } 
});

module.exports = router;