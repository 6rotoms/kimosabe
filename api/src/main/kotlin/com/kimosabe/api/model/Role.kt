package com.kimosabe.api.model

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name="roles")
class Role(
        @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id : Long? = null,
        @Enumerated(EnumType.STRING) var name: RoleName? = null
) : Serializable