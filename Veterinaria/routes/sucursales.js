const express = require('express');
const router = express.Router();
const Sucursal = require('../Models/sucursalesModel');

router.get("/", async (req,res)=>{
    try {
        const sucursales = await Sucursal.find();
        res.json(sucursales);
    } catch (err){
        res.status(500).json({message: err.message});
    }
});

router.get("/:id", async (req,res)=>{
    try {
        const sucursal = await Sucursal.findById(req.params.id);
        if(!sucursal){
            return res.status(404).json({message:"Sucursal no encontrada"});
        } else {
            res.json(sucursal);
        }
    } catch (err){
        res.status(500).json({message: err.message});
    }
});

router.get("/:id/servicios", async (req,res)=>{
    try {
        const sucursal = await Sucursal.findById(req.params.id);
        if(!sucursal){
           return res.status(404).json({message:"Sucursal no encontrada"});
        } else {
            res.json(sucursal.servicios);
        }
    } catch (err){
        res.status(500).json({message: err.message});
    }
});

module.exports = router;