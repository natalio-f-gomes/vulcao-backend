#!/bin/bash
systemctl daemon-reload
systemctl enable vulcao-backend
systemctl start vulcao-backend