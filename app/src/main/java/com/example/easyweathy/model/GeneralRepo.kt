package com.example.easyweathy.model

import com.example.easyweathy.database.LocalSource
import com.example.easyweathy.network.RemoteSource

interface GeneralRepo:RemoteSource,LocalSource {
}