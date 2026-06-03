const express = require('express');
const router = express.Router();
const Usuario = require('../models/userModel');
const authMiddleware = require('./authMiddleware');

router.get("/profile", authMiddleware, async (req, res) => {
    try {
        const user = await Usuario.findById(req.usuarioId).select("-contraseña");
        if (!user) {
            return res.status(404).json({ message: "Usuario no encontrado" });
        }
        res.status(200).json(user);
    } catch (err) {
        res.status(500).json({ message: "Error en el servidor" });
        console.error(err);
    }
});

router.get("/users", async(req,res)=>{
    try {
        const users = await Usuario.find();
        res.status(200).json(users);
    } catch (err) {
        res.status(500).json({ message: "Error en el servidor" });
        console.error(err);
    }
});

module.exports = router;