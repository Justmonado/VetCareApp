const express = require('express');
const router = express.Router();
const Usuario = require('../models/userModel');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');

router.post("/register", async (req,res)=>{
    const {nombre,apellido,fechaNacimiento,nombreUsuario,correo,contraseña} = req.body;
    try{
        const existingUser = await Usuario.findOne({correo});
        const existingUsername = await Usuario.findOne({
            nombreUsuario
        });
        if(existingUser){
            return res.status(400).json({message:"Usuario ya existe"});
        }
        if(existingUsername){
            return res.status(400).json({message:"Nombre de usuario ya existe"});
        }
        const hashedPassword = await bcrypt.hash(contraseña,10);
        const newUser = new Usuario({
            nombre,
            apellido,
            fechaNacimiento,
            nombreUsuario,
            correo,
            contraseña: hashedPassword
        });
        await newUser.save();
        res.status(201).json({message:"Usuario registrado correctamente"});
    }catch(err){
        res.status(500).json({message:"Error en el servidor"});
    }
    
});

router.post("/login", async (req, res) => {
    const { nombreUsuario, contraseña } = req.body;

    try {
        const user = await Usuario.findOne({ nombreUsuario });

        if (!user) {
            return res.status(400).json({ message: "Usuario no encontrado" });
        }

        const isPasswordValid = await bcrypt.compare(
            contraseña,
            user.contraseña
        );

        if (!isPasswordValid) {
            return res.status(400).json({ message: "Contraseña incorrecta" });
        }

        const token = jwt.sign(
            { id: user._id },
            process.env.JWT_SECRET,
            { expiresIn: "1h" }
        );

        res.status(200).json({ token });

    } catch (err) {
        console.log(err); // 🔥 IMPORTANTE PARA VER EL ERROR REAL
        res.status(500).json({ message: err.message });
    }
});

module.exports = router;